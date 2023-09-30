package com.ervelus.marineservice.service;

import ru.egormit.library.MeleeWeaponGroupResponse;

import javax.ejb.Local;

@Local
public interface SpaceMarineGroupService {
    MeleeWeaponGroupResponse groupSpaceMarinesByMeleeWeaponAndCount();
}
