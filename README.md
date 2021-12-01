# FunnyAutoReply

<p float="left">
  <img src="https://user-images.githubusercontent.com/58141904/144222056-2938fba7-5dbf-421c-af92-5d4cf40256f9.png" width="250" >
  <img src="https://user-images.githubusercontent.com/58141904/144222675-eabc7238-f468-4262-a2a5-46b3719e247c.png" width="250" >
  <img src="https://user-images.githubusercontent.com/58141904/144223138-3d8f0885-893e-4521-b8bd-fe41e9dc1f56.png" width="250" >
</p>

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

Dark Mode
