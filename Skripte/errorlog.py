

text="""
Jun 20 01:21:53 sirko-X553MA anacron[31118]: error Job `cron.daily' started
Jun 20 01:21:54 sirko-X553MA anacron[31190]: Updated timestamp for jadsdfsdfsdob `cron.daily' to 2017-06-20
Jun 20 01:22:04 sirko-X553MA cracklib: no dictierroronary ufsfdsfsdfpdate necessary.
"""

f=open('/var/log/syslog.1','a')

for line in text:
	f.write(line)

f.close()

