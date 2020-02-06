xquery version "3.1";

declare namespace reviews = "http://www.scit.org/schema/reviews";
declare namespace review = "http://www.scit.org/schema/review";

for $review in fn:doc("/db/apps/scit/reviews.xml")/reviews:reviews/review:review
where $review/@id = "%1$s"
return $review