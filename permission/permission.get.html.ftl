<html>
  <body>

<#if args.username ??>User access has been provided
 to ${args.username}
<#else>null!</#if>

<br>

<#if testMessage ??> ${testMessage}<#else>null!</#if>

  </body>
</html>