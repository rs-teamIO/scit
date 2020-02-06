xquery version "3.1";

declare namespace users = "http://www.scit.org/schema/users";
declare namespace user = "http://www.scit.org/schema/user";

for $user in fn:doc("/db/apps/scit/users.xml")/users:users/user:user
where $user/user:email = "%1$s"
return $user