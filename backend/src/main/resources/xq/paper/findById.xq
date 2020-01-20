xquery version "3.1";

declare namespace papers = "http://www.scit.org/schema/papers";
declare namespace paper = "http://www.scit.org/schema/paper";

for $paper in fn:doc("/db/apps/scit/papers.xml")/papers:papers/paper:paper
where $paper/@paper:id = "%1$s"
return $paper