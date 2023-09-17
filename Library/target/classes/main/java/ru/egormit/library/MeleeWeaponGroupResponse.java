package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class MeleeWeaponGroupResponse {
    private Long CHAIN_SWORD;
    private Long CHAIN_AXE;
    private Long POWER_FIST;
}
