# FunnyAutoReply

**Bemutatás**

Ha a telefonra érkezik egy hívás és nem veszi fel senki, akkor az alkalmazás küld egy random 
viccet sms-ben a hívó számra. Az sms küldés be/ki kapcsolható, a válaszolandó számok 
csoportja állítható és az elküldött viccek megtekinthetőek. Így legalább ha nem veszem fel a 
telefont, akkor szórakoztathatom a hívó felet.

**Főbb funkciók**

Az alkalmazásban be/ki lehet kapcsolni az sms küldést. – shared preferences

Be lehet állítani, hogy csak akkor küldjön sms-t, amennyiben a szám szerepel a contact-ok 
között és ha szerepel, akkor is csak akkor küldjön, ha kedvencként meg van jelölve. – shared 
preferences

A beállításokat el mentődnek és azok alapján működik az sms küldés. – shared preferences
Az alkalmazás csak nem fogadott hívás (kinyomtuk vagy nem vettük fel) esetén küld sms-t.
– broadcast reciever

Az alkalmazás küld egy viccet sms-ben hálózati kapcsolaton keresztül (egy random joke api 
segítségével). – hálózati kapcsolat

Ki lehet választani néhány kategória közül, hogy milyen típusú viccet szeretnénk küldeni.

Egy history nézetben egy listában megtekinthetők, hogy milyen számra küldtünk viccet és az 
elemekre kattintva megnézhető, hogy mi volt a vicc. – sqlite, recycle view
