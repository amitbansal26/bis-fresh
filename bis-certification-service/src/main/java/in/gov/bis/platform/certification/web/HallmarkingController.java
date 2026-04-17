package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.HallmarkingJeweller;
import in.gov.bis.platform.certification.domain.HallmarkingAhc;
import in.gov.bis.platform.certification.repository.HallmarkingJewellerRepository;
import in.gov.bis.platform.certification.repository.HallmarkingAhcRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hallmarking")
public class HallmarkingController {
    private final HallmarkingJewellerRepository jewRepo;
    private final HallmarkingAhcRepository ahcRepo;
    public HallmarkingController(HallmarkingJewellerRepository jr, HallmarkingAhcRepository ar) { jewRepo=jr; ahcRepo=ar; }
    @GetMapping("/jewellers") public List<HallmarkingJeweller> jewellers() { return jewRepo.findAll(); }
    @PostMapping("/jewellers") public HallmarkingJeweller createJeweller(@RequestBody HallmarkingJeweller j) { return jewRepo.save(j); }
    @GetMapping("/ahc") public List<HallmarkingAhc> ahcs() { return ahcRepo.findAll(); }
    @PostMapping("/ahc") public HallmarkingAhc createAhc(@RequestBody HallmarkingAhc a) { return ahcRepo.save(a); }
}
