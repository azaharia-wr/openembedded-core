KBRANCH ?= "v5.19/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.19/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.19/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.19/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.19/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.19/standard/base"
KBRANCH:qemuriscv32  ?= "v5.19/standard/base"
KBRANCH:qemux86  ?= "v5.19/standard/base"
KBRANCH:qemux86-64 ?= "v5.19/standard/base"
KBRANCH:qemumips64 ?= "v5.19/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "9111e441d26961e67bef7866919c51b27e794bac"
SRCREV_machine:qemuarm64 ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemumips ?= "5f89b6548b6665f3ebec11266f2c7db2793129da"
SRCREV_machine:qemuppc ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemuriscv64 ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemuriscv32 ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemux86 ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemux86-64 ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_machine:qemumips64 ?= "99613988297ac45653cc81319e63b2821645a3ba"
SRCREV_machine ?= "44a446e91acea4f2f0d35896763224c3b52a3ed5"
SRCREV_meta ?= "829511f25a4a868d33019ace607dc7f0a14d3105"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "d1105a680e66b0482bd18048534c58ecabb5c284"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.19/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.19.9"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

