DESCRIPTION = "Compile AT91 board device tree overlays and pack them in a FIT image"
LICENSE = "(GPL-2.0-or-later & MIT)"
LIC_FILES_CHKSUM = "file://COPYING;md5=775626b7bc958bdcc525161f725ece0f \
                    file://LICENSES/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97 \
                    file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

inherit deploy

COMPATIBLE_MACHINE = '(sama5d3-xplained|sama5d3-xplained-sd|at91sam9x5ek|at91sam9x5ek-sd|sama5d4-xplained|sama5d4-xplained-sd|sama5d2-xplained|sama5d2-xplained-sd|sama5d2-xplained-emmc|sama5d2-ptc-ek|sama5d2-ptc-ek-sd|sama5d27-som1-ek|sama5d27-som1-ek-sd|sama5d2-icp-sd|sam9x60ek|sam9x60ek-sd|sama5d27-wlsom1-ek-sd|sama5d27-wlsom1-ek-optee-sd|sama5d29-curiosity-sd|sama7g5ek|sam9x60-curiosity|sam9x60-curiosity-sd|sam9x75-curiosity|sam9x75-curiosity-sd|sam9x75eb|sam9x75eb-sd)'

SRC_URI = "git://github.com/linux4microchip/dt-overlay-mchp.git;protocol=https;branch=master"

PV = "1.0+git${SRCPV}"
SRCREV = "77df5fd07814283d725d65be6d34c72e78ccccd4"

DEPENDS = "virtual/kernel u-boot-mkimage-native dtc-native"

S = "${WORKDIR}/git"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Ensure that the machine is properly set
AT91BOOTSTRAP_MACHINE ??= "${MACHINE}"

do_compile[depends] += "virtual/kernel:do_deploy virtual/kernel:do_shared_workdir"
do_compile[nostamp] = "1"

do_compile () {
    # Check to properly identify the board
    if [ -z "${AT91BOOTSTRAP_MACHINE}" ]; then
        bbnote "No AT91BOOTSTRAP_MACHINE set for ${MACHINE}"
        exit 1
    fi

    if [ -d "${AT91BOOTSTRAP_MACHINE}" ]; then
        bbnote "Compiling DTBOs"
        oe_runmake DTC=dtc KERNEL_DIR=${STAGING_KERNEL_DIR} KERNEL_BUILD_DIR=${KERNEL_PATH} ${AT91BOOTSTRAP_MACHINE}_dtbos
    else
        bbnote "No DTBOs to compile"
    fi

    # Over-ride itb target in Makefile
    if [ -e "${AT91BOOTSTRAP_MACHINE}.its" ]; then
        bbnote "Creating the FIT image"
        DTC_OPTIONS="-Wno-unit_address_vs_reg -Wno-graph_child_address -Wno-pwms_property"
        mkimage -D "-i${DEPLOY_DIR_IMAGE} -p 1000 ${DTC_OPTIONS}" -f ${AT91BOOTSTRAP_MACHINE}.its ${AT91BOOTSTRAP_MACHINE}.itb
    else
        bbnote "No its file to create FIT images"
    fi
}

FILES:${PN} = "/boot/*.*"

addtask install after do_compile

do_install () {
    # Copy files to /boot
    if [ -e ${AT91BOOTSTRAP_MACHINE} ]; then
        install -d ${D}/boot
        install ${AT91BOOTSTRAP_MACHINE}/${AT91BOOTSTRAP_MACHINE}*.dtbo ${D}/boot
    fi;

    if [ -e ${AT91BOOTSTRAP_MACHINE}.itb ]; then
        install -d ${D}/boot
        install ${AT91BOOTSTRAP_MACHINE}.itb ${D}/boot/
        install ${AT91BOOTSTRAP_MACHINE}.its ${D}/boot/
    fi;
}

addtask deploy after do_install

do_deploy () {
    #bbnote "Copying ${fit_image_basename}.itb and source file to ${DEPLOYDIR}..."
    if [ -e ${AT91BOOTSTRAP_MACHINE}.itb ]; then
        install ${AT91BOOTSTRAP_MACHINE}.itb ${DEPLOYDIR}/
        install ${AT91BOOTSTRAP_MACHINE}.its ${DEPLOYDIR}/
    fi;
}
