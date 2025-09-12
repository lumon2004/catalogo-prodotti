document.addEventListener('DOMContentLoaded', () => {
	const prodottiContainer = document.querySelector('.prodotti-container');
	if (!prodottiContainer) return; // Se non ci sono prodotti, esce

	const prodottiNodeList = document.querySelectorAll('.prodotto-blocco');

	// Trasforma NodeList in array con info per ordinamento
	const prodottiArray = Array.from(prodottiNodeList).map(node => ({
		node: node,
		nome: node.querySelector('h3').textContent.trim().toLowerCase(),
		marca: node.querySelector('p:nth-of-type(1) span').textContent.trim().toLowerCase(),
		anno: parseInt(node.querySelector('p:nth-of-type(2) span').textContent.trim()),
		prezzo: parseFloat(
			node.querySelector('.prezzo span')
				.textContent
				.replace(/\./g,'')    // rimuove i punti delle migliaia
				.replace(',', '.')     // sostituisce la virgola con il punto
				.replace(' €','')      // rimuove il simbolo €
		)
	}));

	// Gestione click sui pulsanti filtro
	document.querySelectorAll('.filtro-btn').forEach(btn => {
		btn.addEventListener('click', () => {
			const key = btn.dataset.key;
			let order = btn.dataset.order;

			// Ordina prodotti
			prodottiArray.sort((a, b) => {
				let valA = a[key];
				let valB = b[key];

				// Se sono numeri
				if (typeof valA === 'number' && typeof valB === 'number') {
					return order === 'asc' ? valA - valB : valB - valA;
				}

				// Altrimenti confronto stringhe
				valA = valA.toString();
				valB = valB.toString();
				if (valA < valB) return order === 'asc' ? 1 : -1;   // come facevi tu
				if (valA > valB) return order === 'asc' ? -1 : 1;
				return 0;
			});

			// Aggiorna il container
			prodottiContainer.innerHTML = '';
			prodottiArray.forEach(p => prodottiContainer.appendChild(p.node));

			// Inverti l'ordine per il prossimo click
			btn.dataset.order = order === 'asc' ? 'desc' : 'asc';
			btn.textContent = btn.textContent.slice(0, -1) + (order === 'asc' ? '↓' : '↑');

			// Gestione classe attivo e reset frecce
			document.querySelectorAll('.filtro-btn').forEach(b => {
				if (b !== btn) {
					b.classList.remove('attivo');
					// Reset freccia al valore di default ↑
					b.textContent = b.textContent.slice(0, -1) + '↑';
					// Imposta data-order di tutti gli altri pulsanti a "asc"
					b.dataset.order = 'asc';
				}
			});

			// Aggiunge classe attivo al pulsante cliccato
			btn.classList.add('attivo');
		});
	});
});