// Gestione del form di commento
document.addEventListener('DOMContentLoaded', function() {
	const commentInput = document.getElementById('commentInput');
	const hiddenContent = document.getElementById('hiddenContent');
	const form = document.getElementById('commentForm');
	const submitButton = form.querySelector('button[type="submit"]');
	
	// Inizialmente disabilita il button
	updateButtonState();
	
	// Funzione per aggiornare lo stato del button
	function updateButtonState() {
		const content = commentInput.innerText.trim();
		if (content.length === 0) {
			submitButton.disabled = true;
			submitButton.classList.add('disabled');
		} else {
			submitButton.disabled = false;
			submitButton.classList.remove('disabled');
		}
	}
	
	// Aggiorna il campo nascosto e lo stato del button quando il contenuto cambia
	commentInput.addEventListener('input', function() {
		hiddenContent.value = this.innerText.trim();
		if (commentInput.innerText.trim().length === 0) {
			commentInput.innerHTML = '';
		}
		updateButtonState();
	});
	
	// Aggiorna anche quando si perde il focus (per sicurezza)
	commentInput.addEventListener('blur', function() {
			updateButtonState();
		});
		
		// Validazione al submit
		form.addEventListener('submit', function(e) {
			const content = commentInput.innerText.trim();
			if (!content) {
				e.preventDefault();
				alert('Inserisci un commento prima di inviare!');
				commentInput.focus();
			} else {
				hiddenContent.value = content;
			}
		});
		
		// Gestisce Enter per andare a capo
		commentInput.addEventListener('keydown', function(e) {
			if (e.key === 'Enter' && !e.shiftKey) {
				// Permette di andare a capo con solo Enter
			}
		});
	});

// Funzione per convertire la data in un formato "tempo fa"
function timeAgo(date) {
	const seconds = Math.floor((new Date() - new Date(date)) / 1000);
	const intervals = [
		{ labelSing: 'secondo', labelPlur: 'secondi', seconds: 1 },
		{ labelSing: 'minuto', labelPlur: 'minuti', seconds: 60 },
		{ labelSing: 'ora', labelPlur: 'ore', seconds: 3600 },
		{ labelSing: 'giorno', labelPlur: 'giorni', seconds: 86400 },
		{ labelSing: 'mese', labelPlur: 'mesi', seconds: 2592000 },
		{ labelSing: 'anno', labelPlur: 'anni', seconds: 31536000 }
	];

	for (const i of intervals.reverse()) {
		const count = Math.floor(seconds / i.seconds);
		if (count > 0) {
			return count + ' ' + (count === 1 ? i.labelSing : i.labelPlur) + ' fa';
		}
	}
	return 'adesso';
}

// Aggiorna i commenti con il tempo trascorso
document.addEventListener("DOMContentLoaded", function () {
	document.querySelectorAll('.comment-time').forEach(el => {
		const isoTime = el.getAttribute('data-timestamp');
		el.textContent = timeAgo(isoTime);
	});
});