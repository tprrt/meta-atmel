FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "3eac340a781c00ccd61b151b0e9c22a8c6e9f9f0"

DEPENDS:append = " util-linux-libuuid"
inherit pkgconfig

PV = "4.2.0+git${SRCPV}"

COMPATIBLE_MACHINE = "(sama5d27-som1-ek-optee-sd|sama5d27-wlsom1-ek-optee-sd)"
