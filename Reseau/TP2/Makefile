I=$(shell /sbin/ifconfig eth0 | grep "inet adr" | sed -e 's/.*adr:10\.0\.\([0-9]*\.[0-9]*\).*/\1/')
QEMU=kvm
OPTIONS=-no-kvm -nographic -hda /net/travail/ens/sathibau/images/image-reseau -net socket,mcast=230.0.$I:1234 
run:
	xterm -title machine1 -geometry +0+0 -e $(QEMU) -snapshot $(OPTIONS) -net nic,macaddr=52:54:00:12:34:51 & \
	xterm -title machine2 -geometry +750+0 -e $(QEMU) -snapshot $(OPTIONS) -net nic,macaddr=52:54:00:12:34:52 & \
	xterm -title espion   -geometry +0+400 -e $(QEMU) -snapshot $(OPTIONS) -net nic,macaddr=52:54:00:12:34:59 & \
	wait
