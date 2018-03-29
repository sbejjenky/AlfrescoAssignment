function main()
{

// check that search term has been provided
if (args.username == undefined || args.username.length == 0)
{
   status.code = 400;
   status.message = "Required parameters have not been provided.";
   status.redirect = true;
}
else
{
var  node = search.findNode(args.noderef);

if (node == null || node == undefined)
{
   status.code = 400;
   status.message = "Node not found";
   status.redirect = true;
}


node.setPermission("Read", args.username);
model.permissions = node.getPermissions();
}

}
main();