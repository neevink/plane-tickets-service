package com.ervelus.marineservice.service.impl;

import com.ervelus.marineservice.repository.SpaceMarineGroupRepository;
import com.ervelus.marineservice.service.SpaceMarineGroupService;
import ru.egormit.library.MeleeWeaponGroupResponse;
import ru.egormit.library.enums.MeleeWeapon;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Map;

@Stateless
public class SpaceMarineGroupServiceImpl implements SpaceMarineGroupService {

    @Inject
    private SpaceMarineGroupRepository repository;

    @Override
    public MeleeWeaponGroupResponse groupSpaceMarinesByMeleeWeaponAndCount() {
        Map<MeleeWeapon, Long> meleeToAmount = repository.groupByMeleeAndCount();
        return MeleeWeaponGroupResponse.of(
                meleeToAmount.get(MeleeWeapon.CHAIN_AXE),
                meleeToAmount.get(MeleeWeapon.CHAIN_SWORD),
                meleeToAmount.get(MeleeWeapon.POWER_FIST));
    }
}
