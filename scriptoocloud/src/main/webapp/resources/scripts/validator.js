function validate() {
	let form = document.getElementById("repoForm");
	try {
		let url = new URL(form.url.value);
		let path = url.pathname.split("/");
		if (path.length > 2) {
			form.host.value = url.hostname;
			form.owner.value = path[1];
			form.repo.value = path[2];
			form.submit();
		}
		else
			throw "err";
	}
	catch (err) {
		document.getElementById("errorWindow")
			.textContent =
			"URL must be of the format https://host.domain/owner/repo";
	}
}       
