package ru.ervelus.marineservice.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.egormit.library.MeleeWeaponGroupResponse;
import ru.ervelus.marineservice.service.SpaceMarineGroupService;

@RestController
@RequestMapping(value = "/spacemarines/group", produces = "application/json")
@RequiredArgsConstructor
public class SpaceMarineGroupController extends BaseController {
    private final SpaceMarineGroupService groupService;

    @PostMapping("/melee")
    public ResponseEntity<MeleeWeaponGroupResponse> getNumberOfSpaceMarinesWithEachMeleeType() {
        return ResponseEntity.ok(groupService.groupSpaceMarinesByMeleeWeaponAndCount());
    }
}
