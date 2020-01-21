xquery version "3.1";

declare namespace cover_letters = "http://www.scit.org/schema/cover_letters";
declare namespace cover_letter = "http://www.scit.org/schema/cover_letter";

for $cover_letter in fn:doc("/db/apps/scit/cover_letters.xml")/cover_letters:cover_letters/cover_letter:cover_letter
where $cover_letter/@id = "%1$s"
return $cover_letter