<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="centerdiv">
<h1>Contact US</h1>


<form action="write" method="post" id="faq">
<table class="table">

	<tr class="tr">
		<th class="th" style="border-bottom-color: ">name</th>
		<td class="td"><input type="text" name="fb_user" required="required"></td>
	</tr>
	
	<tr class="tr">
		<th class="th">email</th>
		<td class="td"><input type="email" name="email" style="width:300px;" required="required">
		<br>
		※ check e-mail address.
		</td>
	</tr>
	<tr class="tr">
		<th class="th">Title</th>
		<td class="td"><input type="text" name="title" style="width:300px;" required="required"></td>
	</tr>
	<tr class="tr">
		<th class="th">Content</th>
		<td class="td"><textarea rows="30" cols="100" name="content" required="required"></textarea></td>
	</tr>
	<tr class="tr">
		<td colspan="3" class="white center td">		
		<div><input type="submit" class="btn menubtn submitbtn" value="SUBMIT"></div>  
		<div class="btn menubtn closebtn">CLOSE</div>
		</td>
	</tr>
</table>
</form>

</div>				
</body>
</html>