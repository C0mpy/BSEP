

text="""
2017-06-17 15:15:19 ALLOW TCP 192.168.1.2 172.217.16.110 55662 443 0 - 0 0 0 - - - SEND
2017-06-17 15:15:22 ALLOW TCP 192.168.1.2 54.172.213.155 55663 443 0 - 0 0 0 - - - SEND
2017-06-17 15:15:23 ALLOW TCP 192.168.1.2 40.114.241.141 55664 443 0 - 0 0 0 - - - SEND
2017-06-17 15:15:28 ALLOW UDP 192.168.1.2 239.255.255.250 59562 1900 0 - - - - - - - SEND
2017-06-17 15:15:28 ALLOW TCP 192.168.1.2 40.114.241.141 55665 443 0 - 0 0 0 - - - SEND
2017-06-17 15:15:31 ALLOW TCP 192.168.1.2 204.236.200.114 55666 443 0 - 0 0 0 - - - SEND
2017-06-17 15:15:40 ALLOW TCP 192.168.1.2 172.217.16.110 55667 443 0 - 0 0 0 - - - SENDS


"""

f=open('/var/log/pfirewall','a')

for line in text:
	f.write(line)

f.close()



