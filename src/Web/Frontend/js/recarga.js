	$(document).ready(function(){
		setInterval(
				function(){
					$('#lecturas').load('lecturasREST.php');
				},1500
			);
	});