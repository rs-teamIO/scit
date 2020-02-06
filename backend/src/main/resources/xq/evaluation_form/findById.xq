xquery version "3.1";

declare namespace evaluation_forms = "http://www.scit.org/schema/evaluation_forms";
declare namespace evaluation_form = "http://www.scit.org/schema/evaluation_form";

for $evaluation_form in fn:doc("/db/apps/scit/evaluation_forms.xml")/evaluation_forms:evaluation_forms/evaluation_form:evaluation_form
where $evaluation_form/@evaluation_form:id = "%1$s"
return $evaluation_form