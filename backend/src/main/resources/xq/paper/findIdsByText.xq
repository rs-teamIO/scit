xquery version "3.1";

declare namespace papers = "http://www.scit.org/schema/papers";
declare namespace paper = "http://www.scit.org/schema/paper";
let $uppercase := "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
let $lowercase := "abcdefghijklmnopqrstuvwxyz"

for $paper in fn:doc("/db/apps/scit/papers.xml")/papers:papers/paper:paper
where $paper//text()[fn:contains(
    fn:translate(., $uppercase, $lowercase),
    fn:translate("%1$s", $uppercase, $lowercase))]
return $paper/@paper:id/fn:string()