package com.example.gruppe10_main.dataclasses

import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.App

enum class LostObjectEnum { ACTIVE, STRANDED, ORIGIN }

/**
 * Enum to translate [LeewayObjectTypeMap] to leeway object as string.
 */
enum class LeewayModel {
    PIW {
        override fun toString() = "Person-in-water (PIW), unknown state (mean values)"
    },
    SMALL_BUOYANT {
        override fun toString() = "Medical waste (mean values)"
    },
    LARGE_BUOYANT {
        override fun toString() = ">>Medical waste, vials, large"
    },
    BOAT {
        override fun toString() = "Skiff - modified-v, cathedral-hull, runabout outboard powerboat"
    };

    abstract override fun toString(): String
}

/**
 * Maps items from string resource to respective leeway model.
 */
object LeewayObjectTypeMap {
    private val items: Array<String> = App.getContext().resources.getStringArray(R.array.leeway_items)
    val map = mapOf(
            items[0] to LeewayModel.LARGE_BUOYANT,  // Fender
            items[1] to LeewayModel.LARGE_BUOYANT,  // Blåse
            items[2] to LeewayModel.SMALL_BUOYANT,  // Redningsvest
            items[3] to LeewayModel.SMALL_BUOYANT,  // Drivstofftank med innhold
            items[4] to LeewayModel.LARGE_BUOYANT,  // Drivstofftank uten innhold
            items[5] to LeewayModel.SMALL_BUOYANT,  // Åre
            items[6] to LeewayModel.PIW,            // Fiskestang
            items[7] to LeewayModel.PIW,            // Båtflagg
            items[8] to LeewayModel.SMALL_BUOYANT,  // Båtflaggstand
            items[9] to LeewayModel.PIW,            // Garn
            items[10] to LeewayModel.LARGE_BUOYANT,  // Oppblåsbare gummileker
            items[11] to LeewayModel.SMALL_BUOYANT, // Plastgjenstander
            items[12] to LeewayModel.PIW,           // Klesplagg
            items[13] to LeewayModel.PIW,           // Båtpute
            items[14] to LeewayModel.SMALL_BUOYANT, // Tau
            items[15] to LeewayModel.SMALL_BUOYANT, // Vannski, seilbrett
            items[16] to LeewayModel.BOAT,          // Jolle
    )
}