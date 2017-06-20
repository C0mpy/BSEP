text="""
127.0.0.1 - - [03/Jun/2017:13:43:51 +0200] "GET / HTTP/1.1" 200 1895
127.0.0.1 - - [03/Jun/2017:13:43:52 +0200] "GET /favicon.ico HTTP/1.1" 404 991
127.0.0.1 - - [03/Jun/2017:13:44:23 +0200] "GET /manager/html HTTP/1.1" 404 993


"""

f=open('/var/log/tomcat7/localhost_access_log.2017-04-27.txt','a')

for line in text:
	f.write(line)

f.close()