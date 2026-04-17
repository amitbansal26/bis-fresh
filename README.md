# BIS Manakonline RFP – Requirements Analysis and 100-Phase Implementation Roadmap

## Implemented multi-module Spring Boot project (codebase)

Based on the requirements in this README and the referenced RFP PDF, this repository now includes a Maven multi-module Spring Boot starter aligned to major BIS domain clusters.

### Project modules

| Module | Type | Purpose |
| --- | --- | --- |
| `bis-platform-common` | Shared library | Common domain contracts used across services |
| `bis-platform-events` | Shared library | Common event envelope for Kafka/event-driven integrations |
| `bis-identity-service` | Spring Boot service | User registration, bureau user administration, access foundations (RFP 6.2/6.3) |
| `bis-certification-service` | Spring Boot service | Product certification and scheme operations foundations |
| `bis-laboratory-service` | Spring Boot service | LIMS and LRS service foundations |
| `bis-operations-service` | Spring Boot service | HR, finance, procurement and inventory foundations |
| `bis-integration-service` | Spring Boot service | Integration, notification, and cross-platform interoperability foundations |
| `bis-master-data-service` | Spring Boot service | Master data management for shared reference data (port 8086) |

### Quick start

```bash
mvn clean test
```

Start the local LGTM observability stack (Grafana + Loki + Tempo + Mimir):

```bash
docker compose up -d lgtm
```

- Grafana: `http://localhost:3000` (default login: `admin` / `admin`)
- OTLP endpoints: `localhost:4317` (gRPC), `localhost:4318` (HTTP)

Run any service module:

```bash
mvn -pl bis-identity-service spring-boot:run
```

Each service exposes:

- `GET /api/module-info` for module/RFP mapping metadata
- Actuator health endpoint at `/actuator/health`

### OAuth2 / Keycloak configuration

All secured services now validate OAuth2 access tokens from Keycloak and enforce DPoP proofs on protected APIs.

Set the Keycloak issuer for services:

```bash
export KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/bis
```

Set the UI Keycloak settings:

```bash
export NEXT_PUBLIC_KEYCLOAK_URL=http://localhost:8080
export NEXT_PUBLIC_KEYCLOAK_REALM=bis
export NEXT_PUBLIC_KEYCLOAK_CLIENT_ID=bis-ui
export NEXT_PUBLIC_AUTH_BYPASS=false
```

The UI login now uses OAuth2 Authorization Code + PKCE with DPoP and sends `Authorization: DPoP <access_token>` plus the `DPoP` proof header on API calls.

For local testing without Keycloak, set:

```bash
export NEXT_PUBLIC_AUTH_BYPASS=true
```

When bypass mode is enabled, `/login` allows selecting one or more RFP roles and signs in with a local session-only mock identity.

## 1. Document purpose

This markdown document consolidates the implementation-oriented requirements from the BIS Manakonline RFP into an engineering plan that is easier to execute.

It is **not** a commercial bid response. It is a **delivery and solution-planning artifact** built from the RFP chapters on scope, functional requirements, technical requirements, timelines, SLA, and legacy-system annexures.

## 2. Source baseline

- **RFP title:** Engagement of an Agency for Design and Development of an Enterprise-Grade Unified Digital Platform for BIS (Manakonline Platform)
- **Reference no.:** ITSD/MOP/RFP/2025-26/01
- **Scope span used for analysis:** Scope of Work, FRS, TRS, timelines, SLA, and current-system annexures

## 3. Important interpretation note

The RFP mandates the business and technical outcomes, but it does **not** explicitly mandate Spring Boot, PostgreSQL, or Kafka as the only implementation stack.

For this document, they are treated as the **proposed core implementation stack** because they fit the RFP well:

| Technology | Why it fits the RFP |
| --- | --- |
| Spring Boot | RFP requires modular microservices, secure APIs, containerization, CI/CD, and independent deployability. Spring Boot aligns cleanly with these requirements for service implementation. |
| PostgreSQL | RFP explicitly includes PostgreSQL in CSP managed DB support and existing estate already uses PostgreSQL/EDB PostgreSQL in key systems; PostgreSQL is suitable as the primary transactional store. |
| PostGIS | GIS enablement can be implemented as a PostgreSQL extension without introducing a separate core database. |
| Kafka | RFP requires scalable integration, event-based notifications, Saga/distributed transaction support, real-time/batch ingestion, and cross-module synchronization; Kafka fits these integration and eventing needs. |
| REST/gRPC | RFP mentions REST or gRPC for service communication; Spring Boot services can expose REST externally and gRPC where low-latency internal contracts are useful. |
| Containerization | RFP expects Docker/Kubernetes style deployment; Spring Boot services package well into this model. |

**Default implementation assumption used throughout this document:** unless explicitly stated otherwise, each phase is implemented as one or more **Spring Boot services**, with **PostgreSQL** as the domain data store, and **Kafka** as the event and integration backbone. GIS workloads use **PostgreSQL + PostGIS**.

## 4. RFP facts that shape the delivery model

| Requirement driver | What it means for delivery | RFP reference |
| --- | --- | --- |
| Unified platform objective | Build a unified BIS platform covering web application, mobile application, GIS application, and data migration from legacy systems. | RFP p.22 |
| Turnkey responsibility | Design, development, implementation, go-live, O&M, re-hosting of existing applications, and lab-instrument integration are in scope. | RFP pp.33-34, 53-54 |
| Module breadth | The platform spans 41 top-level modules: 3 shared modules, 12 core activity modules, and 26 support/cross-cutting modules. | RFP pp.61-62 |
| Contract duration | 28 months for design/development/implementation/go-live plus 60 months of O&M = 88 months total. | RFP p.396 |
| Delivery batching | The RFP rollout is divided into 6 implementation batches followed by whole-platform stabilization. | RFP pp.396-400 |
| Architecture direction | The RFP expects microservices, centralized integration, IAM, BPM, DMS/ECM, DevOps, data warehouse/central repository, mobile, AI/ML/NLP, and monitoring. | RFP pp.376-388 |
| Hosting model | MeitY-empanelled cloud in India with hyperscaling, DC/DR, and full data residency in India. | RFP pp.389-391 |
| SLA baseline | 99.5% availability, <=3 seconds response time for 99% requests, RPO <=15 minutes, RTO <=2 hours. | RFP pp.403-404 |
| Migration complexity | Legacy estate is heterogeneous and includes large existing databases, including current Manakonline at 16 TB and e-Office at 3 TB. | RFP p.467 |
| Instrument integration | LIMS integration must handle a long list of loggable lab instruments/equipment (50 listed in the annexure). | RFP pp.468-469 |

## 5. Delivery stance using Spring Boot + PostgreSQL + Kafka

### 5.1 Proposed target platform shape

- **API-first modular platform:** each major functional area is delivered as independently deployable Spring Boot services.
- **PostgreSQL as the primary operational database:** each domain owns its data, with shared master/reference data handled through controlled services rather than a shared monolith.
- **Kafka as the internal event backbone:** used for distributed transactions, notification fan-out, audit event streaming, workflow triggers, background processing, and data synchronization.
- **PostGIS for GIS enablement:** no separate GIS database is required initially if the functional GIS scope remains inside operational and reporting use cases.
- **Parallel migration strategy:** use phased migration with reconciliation, event-driven sync, and controlled cutover wherever legacy continuity is necessary.
- **Containerized deployment:** Spring Boot services packaged for Kubernetes-compatible runtime, aligned to the RFP microservices and containerization direction.

### 5.2 Suggested domain/service grouping

| Domain cluster | Primary functional coverage | Illustrative Kafka events |
| --- | --- | --- |
| Identity and Access | User registration, bureau users, SSO, MFA, RBAC/ABAC, session and token services | identity-created, role-changed, access-granted |
| Master and Reference Data | Product masters, office/location masters, departments, schemes, labs, suppliers, configuration masters | master-updated, reference-published |
| Submission and Workflow | Common application shell, tasks, approvals, escalations, assignments, reminders | application-submitted, task-created, SLA-breached |
| Certification Core – Group A | Scheme I, FMCS, CoC, OTR | license-granted, surveillance-created, complaint-linked |
| Certification Core – Group B | CRS, MSCS, Scheme IX, Scheme VI | application-screened, certificate-issued, amendment-applied |
| Hallmarking | Jewellers, AHC, refineries | registration-updated, fee-due, sample-report-received |
| LIMS and LRS | Lab ops, equipment integration, recognition, audits, QA, samples | sample-received, test-completed, instrument-reading-ingested |
| HR and Establishment | Recruitment, service book, transfer, leave, APAR, pensioners | employee-updated, leave-approved |
| Finance and Accounts | Receipts, reconciliation, refunds, finance workflows | payment-received, refund-processed |
| Procurement and Inventory | PR/PO/GRN/stock/issues/returns/scrap/contracts | PR-raised, stock-received, stock-issued |
| Admin and Facilities | Bookings, canteen, quarters, appointments, attendance, floor/security dashboards | booking-created, visitor-checked-in |
| Content, Library, VRR | Website/CMS, ECM/DMS hooks, library, secure reading room | content-published, VRR-accessed |
| Reporting and Monitoring | Unified dashboards, reports, SLA dashboards, operational monitoring | metric-calculated, report-scheduled |
| AI/ML/NLP and GIS | Screening, parsing, risk scoring, chatbot/search, geotagging, routing, thematic maps | document-parsed, risk-score-generated, location-updated |
| Integration and Notification | API gateway/ESB adapters, NSWS, PFMS/GeM, external portals, email/SMS/WhatsApp | integration-requested, notification-sent |

## 6. Requirements analysis by business and technical area

### 6.1 Functional module analysis

| Module or cluster | Implementation analysis |
| --- | --- |
| User Management | SSO registration, Aadhaar OTP/mobile/email validation, bureau user admin, access control, NSWS application submission bridge. |
| Common Requirements | Reusable application form behaviors, prefill, checklists, document handling, workflow consistency, cross-module standards. |
| BIS Website | Open-source enterprise CMS, multilingual publishing, GIGW/WCAG compliant public website with workflows, search, announcements, content governance. |
| Product Certification (Scheme I) | Grant, change in scope, self-compliance, factory/market surveillance, complaint mgmt, renewal, ROP, SOM/status/ROM, fee, agencies, lot inspection, dashboards. |
| Compulsory Registration Scheme (Scheme II) | Grant, scope/name/address changes, complaint mgmt, renewal, market surveillance, ROP, SOM/status/ROM, amendments, fee, cancellation, CCL, regulator NOC. |
| MSCS (Scheme III) | Application submission, grant, scope change, self-compliance, surveillance, complaint mgmt, recertification, fee, auditor empanelment, appeals, document control. |
| CoC (Scheme IV) | Grant, scope change, self-compliance, factory/market surveillance, complaint mgmt, renewal, ROP, SOM/status/ROM, fee, OSA, dashboards. |
| Scheme VI | Conformity assessment for services as per any standard; should be delivered as a full module within the batch roadmap. |
| Scheme IX (Milk and Milk Products) | Application, grant, scope change, self-compliance, surveillance, complaint mgmt, recertification, fee, outsourced agencies, auditor empanelment, appeals, document control. |
| Scheme X / OTR | Grant/CoC, scope change, renewal operative/deferred, status change, market and factory surveillance, complaint handling, ROP, dashboards. |
| FMCS | Grant, scope change, self-compliance, factory/market surveillance, complaints, renewal, ROP, SOM/status/ROM, fee, OSA, lot inspection, onboarding of new licences. |
| Hallmarking – Jewellers | Grant of registration, outlet add/delete, status change, market surveillance, sample test reports, ROP, complaints, dashboards. |
| Hallmarking – AHC Recognition | Grant, off-site centres, scope change, self-compliance, surveillance, market sample reports, ROP, unsatisfactory audits, fee, SOM, renewal. |
| Hallmarking – Refineries | Grant, scope change, factory surveillance, complaint mgmt, renewal, ROP, SOM/status/ROM, fee, dashboards. |
| LIMS | User mgmt, lab/testing scope, sample handling, testing, equipment management/integration, HR, remnants, QA, store, resource, communication, other portal integration, standards promotion, support. |
| LRS | Recognition, auto-renewal, amendment of scope, suspension/withdrawal, empanelment, auditing, auditor management, complaint/feedback, communication, dashboards. |
| HRD and Establishment | Recruitment, roster, employee registration, transfers, leave, LTC, APAR, promotion/pay fixation, NOC/employee certificates, CGHS, disciplinary, exit, service book, pensioners. |
| Finance and Accounts | Finance management and accounts management, linked to payment gateway and internal finance flows. |
| Procurement / Contract / Inventory | Procurement, contracts, inventory, master data, stock inward, stock mgmt, issue/return, scrap sale, dashboards, document management. |
| Legal and Court Case Management | Case lifecycle, hearing/workflow, evidence/document linkage, status monitoring and reporting. |
| PR Department / Secretariat / IT / Hindi / NITS | Departmental portals and operational modules for internal administrative functions, training and official communications. |
| Complaint Management & Enforcement | Citizen/licensee complaint intake, classification, routing, investigation, escalations, closure and analytics. |
| Facilities & Bookings | Meeting room, car, holiday home, quarter allocation, check-in/appointment, canteen, attendance, floor management, security dashboard. |
| Library / VRR / Student Chapter | Controlled document reading, anti-leak safeguards, library services, academic engagement workflow, assignments and approvals. |
| Unified Dashboard / Reporting | Role-based dashboards, cross-module monitoring, notifications, custom report builder, drill-down analytics, trend analysis. |
| AI/ML/NLP & GIS | Document extraction, screening, risk scoring, anomaly detection, multilingual assistant/search, geotagging, spatial workflows and location intelligence. |
| Miscellaneous / Cross-cutting UX | Dark mode, quick actions, reminders, tutorials, accessibility, audit trail, encryption, government portal integrations, restore recently deleted data. |

### 6.2 Technical and non-functional analysis

| Area | Implementation analysis |
| --- | --- |
| Scope and delivery model | Turnkey design, development, implementation, go-live and 60 months O&M; includes re-hosting of existing portals, data migration, mobile app, GIS app, and lab instrument integrations. |
| Requirement engineering | Onsite NC and BAs, field visits, workshops/interviews, RTM, daily documentation, SRS/SDD, detailed migration plan, monthly project status reports. |
| Architecture | Enterprise architecture aligned to TOGAF/IndEA, modular and extensible microservices, independently deployable services, containerization, HA/DR. |
| Integration framework | Centralized API gateway, ESB-style integration for legacy/external systems, distributed transaction management, secure APIs, event-driven internal integration. |
| IAM and security | SSO, OAuth2/OIDC/SAML, MFA, RBAC/ABAC, audit logs, encryption, CERT-In-aligned security operations, Safe-to-Host certificate, annual audits. |
| Data platform | Central repository/data warehouse, batch and real-time ingestion, metadata/cataloging, retention, masking/anonymization, archival and point-in-time recovery. |
| DMS / ECM | Centralized document and content management with workflows, versioning, metadata, search, multilingual support, auditability and secure mobile/web access. |
| DevSecOps | Centralized CI/CD, source control, automated testing, security scanning, environment-specific config, secrets management, rollback-ready deployments. |
| Mobile and GIS | Android/iOS apps with offline sync, geotagging, scanning, push notifications; GIS for mapping, field operations and spatial intelligence. |
| Hosting | MeitY-empanelled CSP, India data residency, hyperscaling, separate Dev/UAT/Prod/DR environments, migration clause to alternate CSP if obligations are not met. |
| Performance and scale | 3s response time target for 99% requests, 99.5% availability, dynamic scaling, large mixed internal/external user base, 16 TB+ current core data footprint. |
| Testing and quality | Unit, smoke, integration, functional, regression, load/stress/spike/volume, security, cross-browser/device, accessibility and UAT. |
| Operations and SLA | Monthly compliance reporting, helpdesk/ticketing, MOP/cloud monitoring, bug-resolution targets, RPO <= 15 min, RTO <= 2 hrs, penalties tied to O&M payments. |
| Training and change | Module-wise training plans, physical + virtual sessions, user manuals, KT, change request mechanism, ongoing upgrades and enhancements. |

## 7. Legacy-state implications for modernization

| Legacy system | Current-state snapshot | Planning implication |
| --- | --- | --- |
| BIS Website | Apache2 + PHP/WordPress + MySQL/MariaDB, ~21 GB DB, ~20,000 hits/day | Treat as content-centric modernization/re-host path first, then gradual integration into unified IAM/content model. |
| FMCS app | IIS + .NET + MS SQL | Requires technology-bridge migration and careful schema/API translation. |
| HFMS | Tomcat + Java/Struts 1 + PostgreSQL | Good candidate for domain decomposition and controlled data migration. |
| Current Manakonline (PC + Hallmarking + CRS) | Tomcat + Java/J2EE + EDB PostgreSQL, ~16 TB DB, ~75,000 hits/day | This is the heaviest migration workload and should drive the migration architecture. |
| Standards portal | Apache2 + PHP + MySQL/MariaDB | Can be integrated via APIs/content federation before deep consolidation. |
| IRD | Apache + Laravel + MySQL | API-based coexistence first, then domain-led modernization. |
| LIMS | Apache + Laravel + MongoDB + MySQL, ~115 GB DB | Needs connector-heavy integration and staged migration because instrument interfaces matter. |
| e-Office | PostgreSQL + MongoDB, ~3 TB DB | Assume coexistence and selective integration rather than wholesale rebuild in early phases. |

**Migration implication:** the solution is not a greenfield-only exercise. It is a hybrid program combining re-hosting, legacy takeover, phased rebuild, event-driven coexistence, and data migration at scale.

## 8. Alignment with the RFP batch model

| RFP batch | RFP module grouping | Mapped phases in this document |
| --- | --- | --- |
| Batch 1 (RFP T+13) | User Management, Master Data Management, HRD & Establishment, LRS, LIMS | Phases 31-42 |
| Batch 2 (RFP T+16) | Product Certification, FMCS, CoC, Scheme X / OTR, integration with Batch 1 | Phases 43-54 |
| Batch 3 (RFP T+19) | Finance & Accounts, Hallmarking Jewellers/Refinery, Hallmarking AHC, integration with Batches 1-2 | Phases 55-64 |
| Batch 4 (RFP T+21) | CRS, MSCS, Scheme IX, Scheme VI, NITS, Complaint Management, Enforcement Management | Phases 65-75 |
| Batch 5 (RFP T+23) | Legal, Library, VRR, Student Chapter, Procurement/Contract/Inventory, Attendance, Floor, Facilities and booking modules | Phases 76-86 |
| Batch 6 (RFP T+25) | Department-wise portals, IT Services, Hindi Dept, PR, Secretariat, Unified Dashboard/Reporting, AI/ML/NLP & GIS, full integration | Phases 87-94 |
| Complete stabilization (RFP T+28) | Whole-platform stabilization and transition to O&M | Phases 95-100 |

## 9. 100-phase implementation roadmap

### 9.1 How to read the roadmap

- A **phase** here is a delivery increment, not necessarily a calendar month.
- Phases are intentionally smaller than the RFP batches so engineering, testing, and migration can be tracked at a controllable level.
- Unless noted otherwise, each phase results in Spring Boot service increments, PostgreSQL schema/data changes, and Kafka event flows.

| Phase | Name | Primary outcome |
| --- | --- | --- |
| 1 | Program kickoff and contract mobilization | Establish PMO, governance, working cadence, RAID log, and success criteria aligned to SoW and milestone model. |
| 2 | Deploy NC, BAs, architects, and UX team | Place onsite/offsite requirement team, publish escalation matrix, assign module ownership. |
| 3 | Project tooling and reporting setup | Stand up project management tool, MPSR templates, issue workflows, sign-off registers, and document repository. |
| 4 | Current-state application inventory | Assess existing portals, hosting, dependencies, interfaces, and support model for takeover and modernization. |
| 5 | Current-state data estate discovery | Inventory schemas, files, volumes, retention, quality issues, and cross-system dependencies across legacy applications. |
| 6 | Field process discovery | Run HQ/RO/BO/lab/AHC/NITS field workshops to capture business processes, exceptions, and operational pain points. |
| 7 | Requirements traceability baseline | Build RTM mapping business objectives, FRS, TRS, NFRs, and acceptance criteria. |
| 8 | Migration strategy framing | Choose phased vs parallel migration per module, identify cutover windows, and define reconciliation strategy. |
| 9 | Security and privacy baseline | Derive DPDP/IT Act/CERT-In controls, data classification, audit obligations, and secure-by-design controls. |
| 10 | Solution blueprint sign-off | Freeze target architecture principles, delivery batches, environment model, release governance, and approval workflow. |
| 11 | Domain decomposition | Split the platform into bounded contexts and Spring Boot microservice groups for all 41 top-level modules. |
| 12 | Canonical and master data model | Define enterprise entities, shared reference data, master data stewardship, and ownership boundaries. |
| 13 | API design standards | Define REST/gRPC contracts, versioning, error standards, idempotency, and external integration patterns. |
| 14 | Kafka event model | Define topic taxonomy, event naming, retention rules, replay strategy, and choreography approach for Saga-style flows. |
| 15 | Identity foundation | Implement core IAM integration, user lifecycle, SSO flows, session policy, and federation approach. |
| 16 | Authorization model | Implement RBAC/ABAC policies, role hierarchy, delegated administration, and audit-ready permission catalog. |
| 17 | Workflow/BPM foundation | Implement workflow engine adapters, approval states, SLA timers, escalations, and task inbox patterns. |
| 18 | Notification foundation | Implement notification templates, Kafka-driven event alerts, SMS/email/WhatsApp routing, and reminder engine. |
| 19 | Document and content foundation | Implement DMS/ECM integration pattern, metadata model, versioning hooks, and document lifecycle states. |
| 20 | Audit and observability foundation | Implement logs, traces, metrics, immutable audit trail, monitoring dashboards, and alert catalog. |
| 21 | DevSecOps foundation | Implement repo strategy, CI/CD, quality gates, SAST/DAST hooks, secrets, and rollback strategy. |
| 22 | PostgreSQL data architecture | Define per-service PostgreSQL databases/schemas, partitioning, indexing, HA/DR, backup, and PITR patterns. |
| 23 | GIS data foundation | Enable PostgreSQL/PostGIS data model, geotag capture patterns, and spatial query conventions. |
| 24 | Resilience foundation | Implement environment topology, DR replication, backup schedule, restore drills, and capacity planning baseline. |
| 25 | UX and accessibility foundation | Create design system, WCAG 2.1 patterns, multilingual UX model, responsive layout, and dark-mode strategy. |
| 26 | Re-host BIS website | Move current public website stack to target cloud landing zone with security, monitoring, and continuity controls. |
| 27 | Re-host current Manakonline | Move current Manakonline workloads and data paths to target cloud with minimal disruption. |
| 28 | Re-host hallmarking services | Move current hallmarking-related workloads and interfaces to target cloud environment. |
| 29 | Re-host current LIMS | Move legacy LIMS and supporting connectors while preparing coexistence with the new platform. |
| 30 | Re-host e-Office dependencies and stabilize legacy takeover | Stabilize coexistence for dependent legacy applications needed during transformation. |
| 31 | User registration service | Build Spring Boot service for registration, validation, OTP orchestration, and welcome communications. |
| 32 | Bureau user administration | Build internal user onboarding, bureau role assignment, delegated admin, and access provisioning. |
| 33 | NSWS intake and application identity | Implement secure NSWS push/pull, applicant identity linking, and common submission handoff. |
| 34 | Master data service | Implement master records for schemes, products, offices, labs, departments, locations, and reusable lookups. |
| 35 | HR recruitment and roster | Implement recruitment, reservation roster, vacancy workflow, and candidate tracking. |
| 36 | Employee core lifecycle | Implement employee registration, service book, transfer, relieving, and core employee profile flows. |
| 37 | HR operations | Implement leave, LTC, leave encashment, APAR, promotion/pay fixation, NOC/employee certificate, CGHS, disciplinary, exit, and pensioners. |
| 38 | LRS recognition and empanelment | Implement recognition, renewal, scope amendment, suspension/withdrawal, empanelment, and financial assistance flows. |
| 39 | LRS audit and grievance | Implement lab auditing, auditor management, complaints/feedback, communication, and reporting. |
| 40 | LIMS operations core | Implement lab/testing scope, sample handling, testing sections, and primary operational flows. |
| 41 | LIMS equipment and support | Implement equipment management/integration, QA, store, resource, remnants, communication, support, and external portal integration. |
| 42 | Batch 1 integration, migration, UAT, and go-live | Complete batch 1 integration, migration rehearsal, UAT, training, production cutover, and stabilization. |
| 43 | Product certification grant | Implement application, scrutiny, workflow, grant decision, certificate issuance, and applicant tracking. |
| 44 | Product certification post-grant changes | Implement change in scope, renewal, amended standards rollout, and license operations. |
| 45 | Product certification surveillance and compliance | Implement self-compliance, factory surveillance, market surveillance, complaints, ROP, SOM, status change, and ROM. |
| 46 | Product certification commercial and field ops | Implement fee module, outsourced agency field/desk management, lot inspection, dashboards, and reports. |
| 47 | FMCS grant and onboarding | Implement FMCS grant flow and onboarding of new FMCS licences. |
| 48 | FMCS post-grant operations | Implement scope change, self-compliance, factory surveillance, and market surveillance. |
| 49 | FMCS compliance and lifecycle | Implement complaints, renewal, ROP, SOM, status change, and ROM. |
| 50 | FMCS commercial and field ops | Implement fee module, OSA field/desk management, lot inspection, dashboards, and reports. |
| 51 | CoC grant and change management | Implement CoC grant, scope change, renewal, and amended standards handling. |
| 52 | CoC surveillance and field ops | Implement self-compliance, factory surveillance, market surveillance, complaints, ROP, SOM/status/ROM, fees, and OSA. |
| 53 | OTR operations | Implement grant/CoC, scope change, operative/deferred renewal, status change, surveillance, complaints, and dashboards. |
| 54 | Batch 2 integration, migration, UAT, and go-live | Complete batch 2 integration with batch 1, migration, UAT, training, go-live, and stabilization. |
| 55 | Finance management core | Implement finance masters, charting, receivables/payables hooks, and workflow-controlled finance operations. |
| 56 | Accounts and payment reconciliation | Implement payment status ingestion, receipts/invoices, refund handling, ledger postings, and reconciliation. |
| 57 | Hallmarking jeweller registration | Implement jeweller grant/registration, outlet add-delete, and retail/corporate status handling. |
| 58 | Hallmarking jeweller compliance | Implement market surveillance, market sample test reports, ROP, complaints, dashboards, and reports. |
| 59 | AHC recognition grant | Implement AHC recognition grant and off-site centre operations. |
| 60 | AHC scope and surveillance | Implement scope change, self-compliance, surveillance, market sample reports, and ROP. |
| 61 | AHC lifecycle and fee controls | Implement unsatisfactory audit handling, hallmarking fee, self-stop marking, renewal, and amendments. |
| 62 | Refinery module | Implement hallmarking refinery grant, scope, surveillance, complaints, renewal, ROP, status, ROM, fee, and dashboards. |
| 63 | Batch 3 migration and integration | Complete data migration and cross-batch finance/hallmarking integrations. |
| 64 | Batch 3 UAT and go-live | Complete batch 3 UAT, training, security clearance, production go-live, and stabilization. |
| 65 | CRS grant and master flow | Implement CRS grant and foundational lifecycle workflow. |
| 66 | CRS post-grant changes | Implement scope changes, name/address/management/contact changes, and renewal. |
| 67 | CRS surveillance and compliance | Implement complaints, market surveillance, ROP, SOM, status change, ROM, and amendments. |
| 68 | CRS commercial/regulator functions | Implement fee module, cancellation, CCL, regulator NOC, dashboards, and reports. |
| 69 | MSCS certification core | Implement application, grant, scope change, self-compliance, and surveillance. |
| 70 | MSCS governance and document control | Implement complaints, recertification, amendments, fee module, auditor empanelment, appeals, document control, and dashboards. |
| 71 | Scheme IX certification core | Implement application, grant, scope change, self-compliance, surveillance, and complaint management. |
| 72 | Scheme IX governance and document control | Implement recertification, amendments, fees, outsourced agencies, auditor empanelment, appeals, document control, and dashboards. |
| 73 | Scheme VI service conformity module | Implement conformity assessment module for services as per any standard. |
| 74 | NITS, complaint, and enforcement modules | Implement NITS training/internship, enterprise complaint management, and enforcement management. |
| 75 | Batch 4 integration, UAT, and go-live | Complete batch 4 cross-module integration, migration, UAT, training, and go-live. |
| 76 | Legal and court case management | Implement case intake, hearing workflow, notices, document linkage, schedules, and legal reporting. |
| 77 | Library services centre | Implement library membership, catalog/search, issue/return/reservation, and library workflows. |
| 78 | Virtual Reading Room | Implement secure read-only streaming, dynamic watermarking, anti-download/anti-print controls, and access logging. |
| 79 | Student chapter platform | Implement assignments, proposal workflow, approvals, tracking, and recognition analytics. |
| 80 | Procurement and contract lifecycle | Implement PR, approvals, PO, contract management, supplier master, and inbound stock linkage. |
| 81 | Inventory operations | Implement GRN, stock management, issue/return, scrap sales, inventory dashboards, and document management. |
| 82 | Attendance, floor, and security operations | Implement attendance monitoring, floor management, and security dashboard flows. |
| 83 | Facilities booking suite | Implement meeting room booking/opening, car booking, holiday home, and staff quarter allocation. |
| 84 | Appointment and canteen operations | Implement check-in/appointment workflow and canteen management. |
| 85 | Batch 5 integration and analytics | Integrate batch 5 modules with enterprise masters, notifications, and dashboards. |
| 86 | Batch 5 UAT and go-live | Complete batch 5 migration, UAT, training, security clearance, go-live, and stabilization. |
| 87 | Department-wise portals and IT services | Implement departmental portal patterns for HQ/BO/RO/Labs/NITS and IT Services operations. |
| 88 | Hindi department module | Implement Hindi department workflows, dashboards, and reporting needs. |
| 89 | Public relations department | Implement PR workflows, communications, content requests, and reporting. |
| 90 | Bureau secretariat management | Implement secretariat workflows, meeting governance, decisions, and records. |
| 91 | Unified dashboards and reports | Implement role-based dashboards, cross-module monitoring, report builder, drill-down, and scheduled reports. |
| 92 | AI/ML/NLP platform services | Implement document parsing, application screening, risk scoring, anomaly detection, chatbot/search, and model governance. |
| 93 | GIS platform services | Implement geotagging, map layers, spatial search, field routing, and GIS-driven workflows using PostGIS-backed services. |
| 94 | Full-platform integration and batch 6 go-live | Complete all-module integration, batch 6 UAT, full production go-live, and platform stabilization. |
| 95 | Security hardening and certification | Complete VAPT remediation, Safe-to-Host readiness, audit evidence, and production security baselines. |
| 96 | Performance engineering and DR drills | Complete load/stress/spike testing, autoscaling validation, RPO/RTO drills, and resilience tuning. |
| 97 | Training, manuals, and change adoption | Finalize training plans, user manuals, admin runbooks, and adoption support for all stakeholder groups. |
| 98 | Cutover closure and data reconciliation | Finalize parallel-run closure, reconciliation sign-offs, archival, and legacy switch-off decisions. |
| 99 | O&M and helpdesk transition | Stand up helpdesk/ticketing, SLA dashboards, monthly compliance reports, and operational support model. |
| 100 | Continuous improvement and CR operating model | Operationalize structured change requests, upgrade roadmap, release calendar, and long-term optimization. |

## 10. Recommended engineering conventions for this stack

### 10.1 Spring Boot conventions

- Use **domain-oriented services**, not one microservice per screen.
- Separate **command/write services**, **query APIs**, and **event consumers** where workload patterns differ.
- Enforce consistent validation, API error contracts, correlation IDs, and audit interceptors.
- External-facing APIs remain REST-first; internal performance-sensitive integrations can use gRPC where justified.

### 10.2 PostgreSQL conventions

- Prefer **database per bounded context** or **schema per bounded context** depending on operational maturity.
- Use **PostgreSQL partitioning** for very large tables such as applications, payments, audits, notifications, and logs.
- Use **JSONB only for controlled extensibility**, not as a substitute for relational design.
- Use **PostGIS** for geotagging, spatial search, boundary checks, and thematic mapping.
- Maintain a central archival and reporting posture without collapsing all domains back into a monolithic database.

### 10.3 Kafka conventions

- Use Kafka for **event choreography**, **CDC-assisted migration**, **notifications**, **workflow transitions**, **asynchronous integrations**, and **audit streaming**.
- Keep event contracts versioned and schema-governed.
- Use retry, DLQ, replay, and idempotent consumer patterns from day one.
- Do not use Kafka where a synchronous transactional API is truly required.

## 11. Key risks and design cautions

| Risk | Planning response |
| --- | --- |
| Large legacy data footprint | Current Manakonline data volume is already very large, so data quality and reconciliation must be treated as a first-class workstream. |
| Too many modules in one monolith | A single shared database and codebase will create deployment and scaling bottlenecks; domain boundaries must stay enforced. |
| Rebuild-before-rehost temptation | The RFP explicitly requires re-hosting/takeover; immediate full replacement is riskier than controlled coexistence. |
| AI scope over-expansion | AI/ML/NLP should start with document parsing, screening, search, risk scoring, and assistant use cases that have measurable value. |
| GIS scope ambiguity | Keep GIS aligned to field operations, geotagging, thematic layers, and spatial search instead of overbuilding a mapping platform. |
| DMS/ECM underestimation | Document lifecycle, metadata, security, and auditability are central to the platform and should not be treated as a late add-on. |
| O&M handover delay | Helpdesk, SLA dashboards, monitoring, and runbooks must start before final stabilization, not after. |

## 12. Recommended definition of done by major stage

| Stage | Definition of done |
| --- | --- |
| Requirement stage | Approved SRS/SDD/RTM, environment strategy, migration plan, module backlog, acceptance criteria. |
| Development stage | Code complete, API contracts frozen, Kafka events validated, DB migrations scripted, unit/integration tests green. |
| Testing stage | Functional, regression, performance, accessibility, and security evidence complete; defect closure within agreed limits. |
| Migration stage | Dry run complete, reconciliation signed off, delta sync tested, rollback steps rehearsed. |
| Go-live stage | UAT approval, training complete, Safe-to-Host/security clearance, production deployment signed off. |
| Stabilization stage | 3-month stabilization metrics achieved, major defects closed, support model operational, SLA measurement activated. |
| O&M stage | Monthly compliance reporting active, helpdesk operational, monitoring dashboards visible to BIS, change-request engine functioning. |

## 13. Practical recommendation

The best way to execute this RFP with the requested stack is:

1. Use **Spring Boot** to implement the domain services and integration APIs.
2. Use **PostgreSQL** as the primary transactional database across domains, with **PostGIS** for GIS.
3. Use **Kafka** as the platform event bus for cross-module synchronization, notifications, audit streams, and migration coexistence.
4. Respect the RFP’s **6-batch rollout**, but internally govern delivery through the **100 smaller phases** above.
5. Treat **re-hosting, migration, DMS/ECM, IAM, dashboards, and O&M tooling** as early platform foundations, not end-stage tasks.

## Appendix A – Full requirement inventory from implementation chapters

The following inventory is derived from the RFP structure for chapters 5 to 8 and is included so the roadmap remains traceable to the source document.

  - **5.1.** Design, Development, Implementation, Go-live and O&M of MOP _(RFP p.36)_
    - **5.1.1.** Phase – I: Requirement Gathering and Preparation for Data Migration _(RFP p.36)_
    - **5.1.2.** Phase – II: Design, Development, Implementation and Go-live _(RFP p.39)_
  - **5.2.** Phase – III: Operation & Maintenance (O&M) _(RFP p.46)_
    - **5.2.1.** Operation and Maintenance Services _(RFP p.47)_
    - **5.2.2.** System Monitoring _(RFP p.49)_
    - **5.2.3.** Change Request _(RFP p.49)_
  - **5.3.** Re-Hosting & Take Over of O&M Services of Existing Applications _(RFP p.53)_
    - **5.3.1.** Re-Hosting Services _(RFP p.53)_
    - **5.3.2.** Re-Hosting & Take Over of O&M Services _(RFP p.54)_
  - **5.4.** Availability of Requisite Skills _(RFP p.57)_
  - **5.5.** Documentation _(RFP p.58)_
  - **5.6.** Training and Capacity Building _(RFP p.59)_
  - **5.7.** Project Management _(RFP p.59)_
  - **5.8.** Project Monitoring and Reporting _(RFP p.59)_
  - **5.9.** SLA Monitoring and Audit Support _(RFP p.60)_
  - **5.10.** Miscellaneous _(RFP p.60)_
  - **6.1.** Modules Envisaged _(RFP p.61)_
  - **6.2.** User Management Module _(RFP p.62)_
    - **6.2.1.** User Registration _(RFP p.62)_
    - **6.2.2.** Submission of Application ( Integration with NSWS via API ) _(RFP p.63)_
    - **6.2.3.** The Bureau User Registration and Management _(RFP p.63)_
    - **6.2.4.** Access Control _(RFP p.63)_
  - **6.3.** Common Requirements _(RFP p.64)_
  - **6.4.** The Bureau Website _(RFP p.65)_
  - **6.5.** Scheme-I: Product Certification _(RFP p.67)_
    - **6.5.1.** Grant of License _(RFP p.67)_
    - **6.5.2.** Operation of License – Change in Scope (inclusion/Deduction) _(RFP p.76)_
    - **6.5.3.** Operation of License – Self-Compliance cum Verification Report _(RFP p.78)_
    - **6.5.4.** Operation of License – Factory Surveillance (FS) _(RFP p.79)_
    - **6.5.5.** Operation of License – Market Surveillance (MS) _(RFP p.83)_
    - **6.5.6.** Operation of License – Complaint Management _(RFP p.86)_
    - **6.5.7.** Operation of License – Renewal of License (Operative) _(RFP p.87)_
    - **6.5.8.** Review of Performance (ROP) _(RFP p.91)_
    - **6.5.9.** Self-Stop Marking (SOM) _(RFP p.92)_
    - **6.5.10.** Change in License Status _(RFP p.92)_
    - **6.5.11.** Resumption of Marking (ROM) _(RFP p.93)_
    - **6.5.12.** Implementation of Revised Standards and Amendments _(RFP p.93)_
    - **6.5.13.** Fee Module _(RFP p.94)_
    - **6.5.14.** Outsourced Agencies (Field Work) Management _(RFP p.95)_
    - **6.5.15.** Outsourced Agencies (Desk Work) Management _(RFP p.96)_
    - **6.5.16.** Lot Inspection _(RFP p.97)_
    - **6.5.17.** Dashboard and Reporting _(RFP p.99)_
    - **6.5.18.** Other Important Features _(RFP p.100)_
  - **6.6.** Scheme-II: Compulsory Registration (CRS) _(RFP p.103)_
    - **6.6.1.** Grant of License _(RFP p.103)_
    - **6.6.2.** Operation of License – Change in Scope (Inclusion/Reduction) _(RFP p.106)_
    - **6.6.3.** Operation of License – Change in Name, Management, Address, Contact Details _(RFP p.108)_
    - **6.6.4.** Operation of License – Complaint Management _(RFP p.108)_
    - **6.6.5.** Operation of License – Renewal of License _(RFP p.109)_
    - **6.6.6.** Market Surveillance _(RFP p.110)_
    - **6.6.7.** Review of performance (ROP) _(RFP p.111)_
    - **6.6.8.** Self Stop Marking (SOM) _(RFP p.111)_
    - **6.6.9.** Change in License Status _(RFP p.112)_
    - **6.6.10.** Resumption of Marking _(RFP p.112)_
    - **6.6.11.** Implementation of Revised Standards/Amendments/Essential Requirements _(RFP p.112)_
    - **6.6.12.** Fee Module _(RFP p.113)_
    - **6.6.13.** Cancellation Module _(RFP p.113)_
    - **6.6.14.** CCL Module _(RFP p.114)_
    - **6.6.15.** Seeking of NOC from Regulator _(RFP p.115)_
    - **6.6.16.** Dashboard and Reporting _(RFP p.116)_
    - **6.6.17.** Other important Features _(RFP p.116)_
  - **6.7.** Scheme-III: Management System Certification Scheme (MSCS) _(RFP p.118)_
    - **6.7.1.** Application Submission _(RFP p.118)_
    - **6.7.2.** Grant of License _(RFP p.121)_
    - **6.7.3.** Operation of License – Change in Scope (Inclusion/Reduction) _(RFP p.125)_
    - **6.7.4.** Operation of License – Self-Compliance cum Verification Report _(RFP p.125)_
    - **6.7.5.** Surveillance _(RFP p.126)_
    - **6.7.6.** Operation of License – Complaint Management _(RFP p.128)_
    - **6.7.7.** Operation of License – Re-Certification of License (Operative) _(RFP p.128)_
    - **6.7.8.** Implementation of Revised Standards/Amendments _(RFP p.129)_
    - **6.7.9.** Fee Module _(RFP p.130)_
    - **6.7.10.** Auditor Empanelment _(RFP p.131)_
    - **6.7.11.** Appeal Related to MSC Activity _(RFP p.132)_
    - **6.7.12.** Document Control _(RFP p.133)_
    - **6.7.13.** Dashboard and Reporting _(RFP p.134)_
    - **6.7.14.** Other Important Features _(RFP p.135)_
  - **6.8.** Scheme- IV: Certificate of Conformity (CoC) _(RFP p.137)_
    - **6.8.1.** Grant of CoC _(RFP p.137)_
    - **6.8.2.** Operations of CoC – Change in Scope (Inclusion/Reduction) _(RFP p.145)_
    - **6.8.3.** Operations of CoC – Self-Compliance cum Verification Report _(RFP p.146)_
    - **6.8.4.** Operations of CoC – Factory Surveillance (FS) _(RFP p.146)_
    - **6.8.5.** Operations of CoC – Market Surveillance (MS) _(RFP p.150)_
    - **6.8.6.** Operations of CoC – Complaint Management _(RFP p.153)_
    - **6.8.7.** Operations of CoC – Renewal of CoC (Operative) _(RFP p.154)_
    - **6.8.8.** Review of Performance (ROP) _(RFP p.157)_
    - **6.8.9.** Self-Stop Marking _(RFP p.158)_
    - **6.8.10.** Change in CoC Status _(RFP p.158)_
    - **6.8.11.** Resumption of Marking _(RFP p.158)_
    - **6.8.12.** Implementation of Revised Standards/Amendments/Essential Requirements _(RFP p.159)_
    - **6.8.13.** Fee Module _(RFP p.160)_
    - **6.8.14.** OSA Management (Inspection + Market Sample Activities) _(RFP p.161)_
    - **6.8.15.** OSA Management (Documentation Review) _(RFP p.162)_
    - **6.8.16.** Dashboard and Reporting _(RFP p.163)_
    - **6.8.17.** Other important Features _(RFP p.164)_
  - **6.9.** Scheme-VI: Conformity Assessment Scheme for Grant of Certificate of Conformity for Services as per Any Standard _(RFP p.167)_
  - **6.10.** Scheme-IX: Conformity Assessment Scheme for Milk and Milk Products _(RFP p.167)_
    - **6.10.1.** Application Submission _(RFP p.167)_
    - **6.10.2.** Grant of License _(RFP p.171)_
    - **6.10.3.** Operation of License – Change in Scope (Inclusion/Reduction) _(RFP p.174)_
    - **6.10.4.** Operation of License – Self-Compliance cum Verification Report _(RFP p.175)_
    - **6.10.5.** Surveillance _(RFP p.175)_
    - **6.10.6.** Operation of License – Complaint Management _(RFP p.178)_
    - **6.10.7.** Operation of License – Re-Certification of License (Operative) _(RFP p.179)_
    - **6.10.8.** Implementation of Revised Standards/Amendments _(RFP p.180)_
    - **6.10.9.** Fee Module _(RFP p.180)_
    - **6.10.10.** Outsourced Agencies (Field Work) Management _(RFP p.182)_
    - **6.10.11.** Auditor Empanelment _(RFP p.183)_
    - **6.10.12.** Appeal Related to MSC Activity _(RFP p.184)_
    - **6.10.13.** Document Control _(RFP p.185)_
    - **6.10.14.** Dashboard and Reporting _(RFP p.186)_
    - **6.10.15.** Other Important Features _(RFP p.187)_
  - **6.11.** Scheme-X: Electrical Equipment QCO & Machinery and Electrical equipment (Omnibus Technical Regulation (OTR)) _(RFP p.191)_
    - **6.11.1.** Grant of License/CoC _(RFP p.191)_
    - **6.11.2.** Operation of License – Change in Scope (inclusion/reduction) _(RFP p.196)_
    - **6.11.3.** Operation of License – Renewal of License (Operative) _(RFP p.197)_
    - **6.11.4.** Operation of License – Renewal of License (Deferred) _(RFP p.198)_
    - **6.11.5.** Change in License Status _(RFP p.198)_
    - **6.11.6.** Market Surveillance _(RFP p.199)_
    - **6.11.7.** Factory Surveillance _(RFP p.199)_
    - **6.11.8.** Complaint handling _(RFP p.199)_
    - **6.11.9.** Review of Performance (RoP) _(RFP p.199)_
    - **6.11.10.** Dashboard and Reporting _(RFP p.199)_
  - **6.12.** Foreign Manufacturers Certification Scheme (FMCS) _(RFP p.201)_
    - **6.12.1.** Grant of License _(RFP p.201)_
    - **6.12.2.** Operations of License – Change in Scope (Inclusion/Reduction) _(RFP p.211)_
    - **6.12.4.** Operations of License – Factory Surveillance _(RFP p.213)_
    - **6.12.5.** Operations of License – Market Surveillance _(RFP p.217)_
    - **6.12.6.** Operations of License – Complaint Management _(RFP p.220)_
    - **6.12.7.** Renewal of License - Operative _(RFP p.221)_
    - **6.12.8.** Review of Performance (ROP) _(RFP p.223)_
    - **6.12.9.** Self-Stop Marking _(RFP p.224)_
    - **6.12.10.** Change in License Status _(RFP p.224)_
    - **6.12.11.** Resumption of Marking _(RFP p.224)_
    - **6.12.12.** Implementation of Revised Standards/Amendments _(RFP p.225)_
    - **6.12.13.** Fee Module _(RFP p.226)_
    - **6.12.14.** Outsourced Agencies (OSA) Management (Field Work) _(RFP p.227)_
    - **6.12.15.** Outsourced Agency (OSA) Management (Desk Work) _(RFP p.228)_
    - **6.12.16.** Lot Inspection _(RFP p.229)_
    - **6.12.17.** Dashboard and Reporting _(RFP p.231)_
    - **6.12.18.** Other Important Features _(RFP p.232)_
    - **6.12.19.** Onboarding of new FMCS licences _(RFP p.235)_
  - **6.13.** Hallmarking Module – Jewellers Registration _(RFP p.236)_
    - **6.13.1.** Grant of License _(RFP p.236)_
    - **6.13.2.** Operations of Registration – Addition/Deletion of Outlets _(RFP p.237)_
    - **6.13.3.** Operations of Registration – Change status of jeweller - Retail/Corporate _(RFP p.237)_
    - **6.13.4.** Operations of Registration – Market Surveillance _(RFP p.237)_
    - **6.13.5.** Operations of Registration – Market Sample Test Reports _(RFP p.237)_
    - **6.13.6.** Operations of Registration – Review of Performance (ROP) _(RFP p.237)_
    - **6.13.7.** Operation of Registration - Complaint Management _(RFP p.237)_
    - **6.13.8.** Operations of Registration - Dashboard and Reporting _(RFP p.237)_
  - **6.14.** Hallmarking Scheme – AHC Recognition Module _(RFP p.237)_
    - **6.14.1.** Grant of Recognition _(RFP p.237)_
    - **6.14.2.** Operations of Recognition – Grant of Recognition to Off-Site Centre _(RFP p.243)_
    - **6.14.3.** Operations of Recognition – Change in Scope _(RFP p.248)_
    - **6.14.4.** Operations of Recognition – Self-Compliance cum Verification Report _(RFP p.250)_
    - **6.14.5.** Operations of Recognition – AHC Surveillance _(RFP p.251)_
    - **6.14.6.** Operations of Recognition – Market Sample Test Reports _(RFP p.252)_
    - **6.14.7.** Operations of Recognition – Review of Performance (ROP) _(RFP p.252)_
    - **6.14.8.** Operations of Recognition – Unsatisfactory Surveillance Audits _(RFP p.254)_
    - **6.14.9.** Operations of Recognition – Hallmarking Fee _(RFP p.255)_
    - **6.14.10.** Operations of Recognition – Self Stop Marking _(RFP p.256)_
    - **6.14.11.** Renewal of Recognition _(RFP p.256)_
    - **6.14.12.** Implementation of Revised Standards/Amendments _(RFP p.257)_
    - **6.14.13.** Dashboard and Reporting _(RFP p.258)_
  - **6.15.** Hallmarking Module - Refineries _(RFP p.261)_
    - **6.15.1.** Grant of License _(RFP p.261)_
    - **6.15.2.** Operation of License – Change in Scope (Inclusion/Deduction) _(RFP p.261)_
    - **6.15.3.** Operation of License – Factory Surveillance (FS) _(RFP p.261)_
    - **6.15.4.** Operation of License – Complaint Management _(RFP p.261)_
    - **6.15.5.** Operation of License – Renewal of License _(RFP p.261)_
    - **6.15.6.** Review of Performance (ROP) _(RFP p.261)_
    - **6.15.7.** Self-Stop Marking (SOM) _(RFP p.261)_
    - **6.15.8.** Change in License Status _(RFP p.261)_
    - **6.15.9.** Resumption of Marking (ROM) _(RFP p.261)_
    - **6.15.10.** Fee Module _(RFP p.261)_
    - **6.15.11.** Dashboard and Reporting _(RFP p.261)_
  - **6.16.** Laboratory Information Management System (LIMS) _(RFP p.262)_
    - **6.16.1.** User Management Module _(RFP p.262)_
    - **6.16.2.** Lab/Testing Scope Module _(RFP p.263)_
    - **6.16.3.** Sample Handling Module _(RFP p.264)_
    - **6.16.4.** Testing Section Module _(RFP p.265)_
    - **6.16.5.** Equipment Management Module _(RFP p.266)_
    - **6.16.6.** Equipment Integration Module _(RFP p.267)_
    - **6.16.7.** Human resource module _(RFP p.267)_
    - **6.16.8.** Remnants Module _(RFP p.268)_
    - **6.16.9.** QA Module _(RFP p.269)_
    - **6.16.10.** Store Module _(RFP p.270)_
    - **6.16.11.** Resource module _(RFP p.271)_
    - **6.16.12.** Communication Module _(RFP p.272)_
    - **6.16.13.** Integration with Other Portals/Systems _(RFP p.273)_
    - **6.16.14.** Standards Promotion Module _(RFP p.274)_
    - **6.16.15.** Support Module _(RFP p.275)_
    - **6.16.16.** Dashboard and Reporting _(RFP p.276)_
    - **6.16.17.** Integration with the Bureau Laboratory Equipment and Instruments _(RFP p.277)_
  - **6.17.** Laboratory Recognition Scheme (LRS) _(RFP p.278)_
    - **6.17.1.** User Management Module _(RFP p.278)_
    - **6.17.2.** Recognition of Lab _(RFP p.278)_
    - **6.17.3.** Auto-renewal of recognition _(RFP p.280)_
    - **6.17.4.** Inclusion/ amendment of scope of recognition _(RFP p.280)_
    - **6.17.5.** Suspension/ withdrawal/ expiry of recognition _(RFP p.281)_
    - **6.17.6.** Empanelment of Lab _(RFP p.282)_
    - **6.17.7.** Auto-renewal of empanelment _(RFP p.284)_
    - **6.17.8.** Inclusion/ amendment of scope of empanelment _(RFP p.284)_
    - **6.17.9.** Suspension/ withdrawal/ expiry of empanelment _(RFP p.285)_
    - **6.17.10.** Any other category / Application for recognition _(RFP p.286)_
    - **6.17.11.** Financial Assistance Module _(RFP p.287)_
    - **6.17.12.** Auditing of Labs _(RFP p.289)_
    - **6.17.13.** Auditor Management _(RFP p.289)_
    - **6.17.14.** Complaint/ feedback handling _(RFP p.290)_
    - **6.17.15.** Communication Module _(RFP p.291)_
    - **6.17.16.** Dashboard and Reporting _(RFP p.292)_
  - **6.18.** HRD and Establishment _(RFP p.293)_
    - **6.18.1.** Recruitment Module _(RFP p.293)_
    - **6.18.2.** Reservation Roaster _(RFP p.294)_
    - **6.18.3.** Registration of Employees (Employee Registration) _(RFP p.294)_
    - **6.18.4.** Transfer Request _(RFP p.296)_
    - **6.18.5.** Transfer Due for Annual Rotation _(RFP p.297)_
    - **6.18.6.** Relieving _(RFP p.299)_
    - **6.18.7.** Leave Management _(RFP p.300)_
    - **6.18.8.** Leave Encashment _(RFP p.302)_
    - **6.18.9.** Leave Travel Concession (LTC) _(RFP p.304)_
    - **6.18.10.** Annual Performance Assessment Report (APAR) _(RFP p.305)_
    - **6.18.11.** Promotion Management & Pay Fixation _(RFP p.306)_
    - **6.18.12.** No Objection Certificates (NOC)/Employee Certificate _(RFP p.308)_
    - **6.18.13.** Central Government Health Scheme (CGHS) _(RFP p.309)_
    - **6.18.14.** Disciplinary Proceedings _(RFP p.310)_
    - **6.18.15.** Exit Process _(RFP p.311)_
    - **6.18.16.** Service Book _(RFP p.312)_
    - **6.18.17.** Pensioners Management _(RFP p.313)_
    - **6.18.18.** Dashboard and Reporting _(RFP p.317)_
  - **6.19.** Finance and Accounts Management _(RFP p.318)_
    - **6.19.1.** Finance Management _(RFP p.318)_
    - **6.19.2.** Accounts Management _(RFP p.320)_
  - **6.20.** Procurement, Contract & Inventory Management _(RFP p.322)_
    - **6.20.1.** Procurement Management _(RFP p.322)_
    - **6.20.2.** Contract Management System _(RFP p.323)_
    - **6.20.3.** Inventory Management _(RFP p.323)_
    - **6.20.4.** Master Data Management _(RFP p.324)_
    - **6.20.5.** Procurement & Stock Inward _(RFP p.324)_
    - **6.20.6.** Stock Management _(RFP p.324)_
    - **6.20.7.** Internal Issue & Return _(RFP p.325)_
    - **6.20.8.** Scrap Sales Management _(RFP p.325)_
    - **6.20.9.** Dashboard and Reporting _(RFP p.326)_
    - **6.20.10.** Document Management _(RFP p.326)_
  - **6.21.** Legal and Court Case Management _(RFP p.326)_
  - **6.22.** Public Relations Department _(RFP p.328)_
  - **6.23.** Bureau Secretariat Management _(RFP p.330)_
  - **6.24.** Complaint Management _(RFP p.331)_
  - **6.25.** Department/Meeting Room Booking and Opening _(RFP p.332)_
  - **6.26.** Car Booking _(RFP p.333)_
  - **6.27.** Holiday Home Booking _(RFP p.335)_
  - **6.28.** The Bureau (Officer/Staff) Quarter Allocation _(RFP p.336)_
  - **6.29.** Appointment Management (Check-in System) _(RFP p.338)_
  - **6.30.** Canteen Management _(RFP p.340)_
  - **6.31.** Attendance Monitoring _(RFP p.341)_
  - **6.32.** Security Management Dashboard _(RFP p.343)_
  - **6.33.** Floor Management _(RFP p.344)_
  - **6.34.** Hindi Department _(RFP p.345)_
    - **6.34.1.** Overview _(RFP p.345)_
    - **6.34.2.** Head of the Department _(RFP p.346)_
    - **6.34.3.** Other Officers of Hindi Dept _(RFP p.347)_
    - **6.34.4.** Other Departments: _(RFP p.348)_
  - **6.35.** National Institute of Training for Standardization (NITS) _(RFP p.350)_
    - **6.35.1.** Training Sub-Module _(RFP p.350)_
    - **6.35.2.** Internship Sub-Module _(RFP p.352)_
  - **6.36.** Enforcement Management _(RFP p.356)_
  - **6.37.** IT Services Department _(RFP p.358)_
  - **6.38.** Library Services Centre _(RFP p.362)_
  - **6.39.** Unified Centralized Dashboard and Reporting _(RFP p.364)_
    - **6.39.1.** Centralized Unified Dashboard _(RFP p.364)_
    - **6.39.2.** Centralized Unified Monitoring _(RFP p.365)_
    - **6.39.3.** Centralized Unified Reporting _(RFP p.365)_
  - **6.40.** AI/ML/NLP & GIS Enablement _(RFP p.366)_
    - **6.40.1.** AI/ML/NLP Enablement _(RFP p.366)_
    - **6.40.2.** Geographic Information System (GIS) Enablement _(RFP p.368)_
  - **6.41.** Virtual Reading Room (VRR) _(RFP p.370)_
  - **6.42.** Standard Promotion: Student Chapter _(RFP p.371)_
  - **6.43.** Miscellaneous Requirements _(RFP p.372)_
  - **7.1.** Design, Development and Implementation _(RFP p.374)_
  - **7.2.** Integration with the Bureau Laboratory Equipment and Instruments _(RFP p.375)_
  - **7.3.** Centralized Technical Components and Infrastructure Requirements _(RFP p.376)_
    - **7.3.1.** Integration and Transaction Management Framework _(RFP p.376)_
    - **7.3.2.** Microservices Architecture _(RFP p.378)_
    - **7.3.3.** Centralized Identity and Access Management (IAM) _(RFP p.378)_
    - **7.3.4.** Workflow & Business Process Management (BPM) Engine _(RFP p.379)_
    - **7.3.5.** Datawarehouse / Central Repository _(RFP p.380)_
    - **7.3.6.** Centralized Enterprise Content Management System (ECM) _(RFP p.380)_
    - **7.3.7.** Centralized Document Management System (DMS) _(RFP p.381)_
    - **7.3.8.** Centralized DevOps Toolchain _(RFP p.382)_
    - **7.3.9.** Project Management Tool (PMT) _(RFP p.382)_
    - **7.3.10.** Enterprise Grade Centralized Collaboration Tool Suite _(RFP p.383)_
    - **7.3.11.** Secure Cloud Storage _(RFP p.383)_
    - **7.3.12.** Helpdesk _(RFP p.384)_
    - **7.3.13.** Centralized Mobile Application Framework _(RFP p.385)_
    - **7.3.14.** Payment Gateway Integration _(RFP p.385)_
    - **7.3.15.** Centralized AI/ML/NLP Enablement _(RFP p.386)_
    - **7.3.16.** Multilingual Support _(RFP p.386)_
    - **7.3.17.** Centralized Audit, Logging & Monitoring _(RFP p.387)_
    - **7.3.18.** Backup, Archival & Legacy Data Management _(RFP p.388)_
    - **7.3.19.** Scheduling & Calendar System _(RFP p.388)_
  - **7.4.** Hosting _(RFP p.389)_
    - **7.4.1.** Cloud Platform _(RFP p.389)_
    - **7.4.2.** Mandatory Security Services from CSP _(RFP p.389)_
    - **7.4.3.** Cloud Infrastructure Capabilities _(RFP p.389)_
    - **7.4.4.** Deployment Responsibilities _(RFP p.390)_
    - **7.4.5.** DC-DR Setup _(RFP p.390)_
    - **7.4.6.** Cloud Services Monitoring and Accountability _(RFP p.390)_
    - **7.4.7.** Scalability and Load Requirements _(RFP p.390)_
    - **7.4.8.** Backup and Disaster Recovery Plan _(RFP p.391)_
    - **7.4.9.** Performance and Migration Clause _(RFP p.391)_
  - **7.5.** Solution Architecture _(RFP p.391)_
    - **7.5.1.** Modular and Extensible Design _(RFP p.391)_
    - **7.5.2.** Customizability _(RFP p.391)_
    - **7.5.3.** Scalability and Performance _(RFP p.391)_
    - **7.5.4.** Microservices and Containerization _(RFP p.391)_
    - **7.5.5.** High Availability and Disaster Resilience _(RFP p.392)_
  - **7.6.** User Interface and Access _(RFP p.392)_
  - **7.7.** Accessibility _(RFP p.392)_
  - **7.8.** Upgrades and Enhancements _(RFP p.392)_
  - **7.9.** Solution Security _(RFP p.393)_
  - **7.10.** Data Security and Privacy _(RFP p.393)_
  - **7.11.** Data Archival or Restore _(RFP p.394)_
  - **8.1.** Contract Period _(RFP p.396)_
  - **8.2.** Project Timelines, Deliverables & Payment Schedule _(RFP p.396)_
    - **8.2.1.** Payment Conditions _(RFP p.401)_
  - **8.3.** Liquidated Damages _(RFP p.402)_

## Appendix B – High-level traceability between this roadmap and the RFP

| RFP source area | Primary roadmap impact |
| --- | --- |
| Ch. 5 Scope of Work | Phases 1-30 and phases 95-100 |
| Ch. 6 Functional Requirements | Phases 31-94 |
| Ch. 7 Technical Requirements | Phases 11-25 and phases 91-96 |
| Ch. 8 Timelines and deliverables | Batch mapping and phase group boundaries |
| Ch. 9 SLA | Phases 95-100 and O&M operating model |
| Annexure XX / XXI | Phases 4-8, 28-30, 40-42, 93, 98 |

---

Prepared as an implementation-planning markdown artifact based on the BIS Manakonline RFP, using **Spring Boot + PostgreSQL + Kafka** as the proposed core delivery stack.
