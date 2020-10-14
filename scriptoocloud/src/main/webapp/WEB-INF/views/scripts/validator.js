function validate()
{   
	let form = document.getElementById("repoForm");
            
     try{
     	let url = new URL(form.userIn.value)
         owner = url. 
         path = 
         form.submit();
         }
         catch(err){
         	console.log("err")
            document.getElementById("errorWindow")
            	.textContent = 
                	"URL must be of the format https://host.domain or https://www.host.domain"
         }
}       