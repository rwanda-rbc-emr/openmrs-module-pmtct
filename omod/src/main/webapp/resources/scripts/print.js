/*
 * helps to print the content of a given DIV
 * 
 */

function printContent(div)
{
	var DocumentContainer = document.getElementById("div");
	var WindowObject = window.open(”, "PrintWindow","width=750,height=650,top=50,left=50,toolbars=no,scrollbars=yes,status=no,resizable=yes");
	WindowObject.document.writeln(DocumentContainer.innerHTML);
	WindowObject.document.close();
	WindowObject.focus();
	WindowObject.print();
	WindowObject.close();
}