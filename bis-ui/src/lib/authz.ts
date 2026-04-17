export type AppRole =
  | 'APPLICANT'
  | 'ADMIN'
  | 'DG'
  | 'DDGR'
  | 'HOD'
  | 'HBO'
  | 'DO'
  | 'IO'
  | 'MSCOR'
  | 'AUDITOR'
  | 'HEAD_LAB'
  | 'OIC_SAMPLE_CELL'
  | 'OIC_TESTING_SECTION'
  | 'OIC_QA'
  | 'OIC_REMNANT_SAMPLE_CELL'
  | 'INTERN'
  | 'TESTING_PERSONNEL'
  | 'PS_DDGL_HEAD_LAB'
  | 'ALTERNATE_OIC_TESTING'
  | 'ALTERNATE_OIC_QA'
  | 'OIC_STORE'
  | 'HR_OFFICER'

export type AppModule =
  | 'dashboard'
  | 'certification'
  | 'hallmarking'
  | 'laboratory'
  | 'lrs'
  | 'hr'
  | 'finance'
  | 'procurement'
  | 'legal'
  | 'facilities'
  | 'master-data'
  | 'notifications'
  | 'integration'

export const RFP_ROLES: { id: AppRole; label: string }[] = [
  { id: 'APPLICANT', label: 'Applicant' },
  { id: 'ADMIN', label: 'Admin' },
  { id: 'DG', label: 'DG' },
  { id: 'DDGR', label: 'DDGR' },
  { id: 'HOD', label: 'HoD' },
  { id: 'HBO', label: 'HBO' },
  { id: 'DO', label: 'DO' },
  { id: 'IO', label: 'IO' },
  { id: 'MSCOR', label: 'MSCOR' },
  { id: 'AUDITOR', label: 'Auditor' },
  { id: 'HEAD_LAB', label: 'Head of Laboratory' },
  { id: 'OIC_SAMPLE_CELL', label: 'OIC Sample Cell' },
  { id: 'OIC_TESTING_SECTION', label: 'OIC Testing Section' },
  { id: 'OIC_QA', label: 'OIC Quality Assurance (QA)' },
  { id: 'OIC_REMNANT_SAMPLE_CELL', label: 'OIC Remnant Sample Cell' },
  { id: 'INTERN', label: 'Interns' },
  { id: 'TESTING_PERSONNEL', label: 'Testing Personnel' },
  { id: 'PS_DDGL_HEAD_LAB', label: 'PS to DDGL / Head Lab' },
  { id: 'ALTERNATE_OIC_TESTING', label: 'Alternate OIC Testing' },
  { id: 'ALTERNATE_OIC_QA', label: 'Alternate OIC-QA' },
  { id: 'OIC_STORE', label: 'OIC (Store)' },
  { id: 'HR_OFFICER', label: 'HR Officer' },
]

const ROLE_ALIASES: Record<string, AppRole> = {
  APPLICANT: 'APPLICANT',
  ADMIN: 'ADMIN',
  DG: 'DG',
  DDGR: 'DDGR',
  HOD: 'HOD',
  HBO: 'HBO',
  DO: 'DO',
  IO: 'IO',
  MSCOR: 'MSCOR',
  AUDITOR: 'AUDITOR',
  HEAD_LAB: 'HEAD_LAB',
  HEAD_OF_LAB: 'HEAD_LAB',
  HEAD_OF_LABORATORY: 'HEAD_LAB',
  OIC_SAMPLE_CELL: 'OIC_SAMPLE_CELL',
  OIC_TESTING_SECTION: 'OIC_TESTING_SECTION',
  OIC_QA: 'OIC_QA',
  OIC_QUALITY_ASSURANCE: 'OIC_QA',
  OIC_REMNANT_SAMPLE_CELL: 'OIC_REMNANT_SAMPLE_CELL',
  INTERN: 'INTERN',
  INTERNS: 'INTERN',
  TESTING_PERSONNEL: 'TESTING_PERSONNEL',
  TP: 'TESTING_PERSONNEL',
  PS_TO_DDGL_HEAD_LAB: 'PS_DDGL_HEAD_LAB',
  PS_DDGL_HEAD_LAB: 'PS_DDGL_HEAD_LAB',
  ALTERNATE_OIC_TESTING: 'ALTERNATE_OIC_TESTING',
  ALTERNATE_OIC_QA: 'ALTERNATE_OIC_QA',
  OIC_STORE: 'OIC_STORE',
  HR_OFFICER: 'HR_OFFICER',
}

const MODULE_ROLE_ACCESS: Record<AppModule, AppRole[]> = {
  dashboard: RFP_ROLES.map(role => role.id),
  certification: ['APPLICANT', 'IO', 'DO', 'HBO', 'DDGR', 'DG', 'MSCOR'],
  hallmarking: ['APPLICANT', 'IO', 'DO', 'HBO', 'DDGR', 'DG'],
  laboratory: [
    'HEAD_LAB',
    'OIC_SAMPLE_CELL',
    'OIC_TESTING_SECTION',
    'OIC_QA',
    'OIC_REMNANT_SAMPLE_CELL',
    'INTERN',
    'TESTING_PERSONNEL',
    'PS_DDGL_HEAD_LAB',
    'ALTERNATE_OIC_TESTING',
    'ALTERNATE_OIC_QA',
    'OIC_STORE',
    'DDGR',
    'DG',
  ],
  lrs: ['APPLICANT', 'AUDITOR', 'MSCOR', 'DDGR', 'DG'],
  hr: ['ADMIN', 'HR_OFFICER', 'HOD', 'DDGR', 'DG'],
  finance: ['ADMIN', 'HOD', 'DDGR', 'DG', 'DO'],
  procurement: ['ADMIN', 'HOD', 'DDGR', 'DG', 'DO'],
  legal: ['ADMIN', 'HOD', 'DDGR', 'DG', 'DO', 'IO'],
  facilities: ['ADMIN', 'HOD', 'DDGR', 'DG'],
  'master-data': ['ADMIN', 'HOD', 'DDGR', 'DG'],
  notifications: ['ADMIN', 'HOD', 'DDGR', 'DG', 'DO'],
  integration: ['ADMIN', 'HOD', 'DDGR', 'DG'],
}

const CROSS_MODULE_ROLES: AppRole[] = ['ADMIN', 'DG']

function normalizeRoleName(input: string) {
  return input.trim().toUpperCase().replace(/^ROLE_/, '').replace(/[^A-Z0-9]+/g, '_')
}

export function toAppRole(input: string): AppRole | null {
  const normalized = normalizeRoleName(input)
  return ROLE_ALIASES[normalized] ?? null
}

export function normalizeRoles(input: string[]) {
  return Array.from(
    new Set(
      input
        .map(role => toAppRole(role))
        .filter((role): role is AppRole => role !== null),
    ),
  )
}

export function getRoleLabel(role: AppRole) {
  return RFP_ROLES.find(item => item.id === role)?.label ?? role
}

export function canAccessModule(roles: AppRole[], module: AppModule, activeRole?: AppRole | null) {
  if (roles.length === 0) {
    return false
  }

  const effectiveRoles = activeRole && roles.includes(activeRole) ? [activeRole] : roles
  const allowedRoles = MODULE_ROLE_ACCESS[module]

  return effectiveRoles.some(role => CROSS_MODULE_ROLES.includes(role) || allowedRoles.includes(role))
}

export function getAccessibleModules(roles: AppRole[], activeRole?: AppRole | null) {
  return (Object.keys(MODULE_ROLE_ACCESS) as AppModule[]).filter(module => canAccessModule(roles, module, activeRole))
}
