# BSEP
Za bezbednost aplikacije odradjeno je:

## Generisanje kljuca i samopotpisivanje sertifikata komande:
	- keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 4000 - generise se kljuc i sertifikat i smestaju u keystore, algoritam za kljuc RSA duzina kljuca 2048bit smesta se u PKCS12 keystore sifra: timsedam
	- dobijeni kljuc smesta se u springboot projekat src/main/resources/keystore/
	- podesava se application.properties da bi tomcat koristio dobijeni kljuc za HTTPS/TLC:
		server.port=8443
		server.ssl.key-store=src/main/resources/keystore/keystore.p12
		server.ssl.key-store-password=timsedam
		server.ssl.keyStoreType=PKCS12
		server.ssl.keyAlias=tomcat

	- dobijeni kljuc je samopotpisan pa mu chrome ne veruje, za kljuc potpisan od Certificte Authority treba imati domen, hosting i koristiti npr certbot, tada se dobije pravi kljuc koji se moze koristiti za bezbednu upotrebu na netu
	- posto je samopotpisan ni java mu ne veruje pa siem_agent nece moci da komunicira sa siem_centrom preko tls po defaultu, potrebno je odraditi sledece komande:
		* openssl x509 -in <(openssl s_client -connect [localhost:port_za_siem_centar] -prexit 2>/dev/null) -out ~/bezbednost.crt
			preuzima se sertifikat sa siem_centra i smesta ~/bezbednost.crt
		* keytool -import -trustcacerts -keystore [putanja_do_jave_]/jre/lib/security/cacerts -noprompt -alias tomcat -storepass changeit -file ~/bezbednost.crt
			ucitava se sertifikat u javin keystore i postavlja kao trusted

## OAuth2 login preko Google
	- na https://console.developers.google.com/apis/credentials se naprave novi credentials za OAuth ClientID, web app, Authorized JavaScript origins https://localhost:8443
	dobijeni: 
		* client ID: 147466579705-8m7nf0agd9mk6f4af519t98mdb3sqtlu.apps.googleusercontent.com
		* client secret: WCt-jHGR16onhWzQ0gNHB2Dj
	- dodata biblioteka u projekat <script src="https://apis.google.com/js/platform.js" async defer></script>
	- dodata informacija o client ID u projekat <meta name="google-signin-client_id" content="YOUR_CLIENT_ID.apps.googleusercontent.com">
	- dodat je google sign in dugmic, skinut sa bowera jer originalni google dugmic ima konflikt sa angularjs
	- dobijene informacije od google se koriste za register u bazi i autorizaciju

## RBAC & Hash + Salt
	- dodate Permission i Role klase koje su uvezane sa User-om
	- napravljen Security Configuration sa sledecim podesavanjima:
		* u configure(AuthenticationManagerBuilder auth) definisano je koriscenje BCrypt kod autentikacije korisnika i nasa implementacija UserDetailsServiceImpl za dobavljanje korisnika
		* u configure(HttpSecurity httpSecurity):
			- sessionCreationPolicy postavljen na STATELESS sto znaci da aplikacija nece praviti nikakvu sesiju. Koristi se zato sto koristimo JSON Web Tokens koji se salju uz svaki zahtev i proveravaju, pa nema razloga za cuvanjem sesije o korisnicima na serveru
			- dodat nas authenticationTokenFilterBean filter u lanac filtera koji je zaduzen za autentikaciju korisnika
			- Json Web Tokeni za autentikaciju/autorizaciju

## Cross-Site Request Forgery
	- u Security Configuration oznacujemo da ce se CSRF token nalaziti u 'X-XSRF-TOKEN' polju http zaglavlja
	- dodata CsrfHeaderFilter klasa koja u cookie postavlja CSRF token
