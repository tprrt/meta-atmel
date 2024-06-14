FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS:append = " openssl"

SRCREV = "526d5bac1b65f907f67c05cd07beca72fbab88dd"

PV = "4.2.0+git${SRCPV}"

COMPATIBLE_MACHINE = "(sama5d27-som1-ek-optee-sd|sama5d27-wlsom1-ek-optee-sd)"
