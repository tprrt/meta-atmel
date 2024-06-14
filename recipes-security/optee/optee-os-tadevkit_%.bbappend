FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "16fbd46d245d634778b9df729e3909d6bfd9a79b"

PV = "4.2.0+git${SRCPV}"

OPTEEMACHINE = "sam"

DEPENDS:append = " dtc-native"

COMPATIBLE_MACHINE = "(sama5d27-som1-ek-optee-sd|sama5d27-wlsom1-ek-optee-sd)"
