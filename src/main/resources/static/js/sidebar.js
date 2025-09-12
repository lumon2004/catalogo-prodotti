/* Script per gestione visibilità sidebar principale */
function toggleSidebar() {
	const sidebar = document.getElementById("sidebar");
	sidebar.classList.toggle("active");
}

/* Script per gestione visibilità sidebar secondaria */
document.addEventListener('DOMContentLoaded', () => {
	const items = document.querySelectorAll('.sidebar-item[data-submenu]');
	const secondary = document.getElementById('sidebar-secondary');
	const secondaryInner = secondary.querySelector('.secondary-inner');
	const sidebar = document.getElementById('sidebar');

	function lockBodyScroll() {
		document.documentElement.style.overflow = 'hidden';
	}
	function unlockBodyScroll() {
		document.documentElement.style.overflow = '';
	}

	function openSecondaryFor(item) {
		const submenuId = 'submenu-' + item.dataset.submenu;
		document.querySelectorAll('.submenu').forEach(sm => sm.style.display = 'none');
		const submenu = document.getElementById(submenuId);
		if (!submenu) return;
		submenu.style.display = 'block';
		secondary.classList.add('active');
		secondary.setAttribute('aria-hidden', 'false');
		lockBodyScroll();

		secondaryInner.scrollTop = 0;
	}

	function closeSecondary() {
		secondary.classList.remove('active');
		document.querySelectorAll('.submenu').forEach(sm => sm.style.display = 'none');
		secondary.setAttribute('aria-hidden', 'true');
		unlockBodyScroll();
	}

	/* --- apertura al passaggio o click --- */
	items.forEach(item => {
		item.addEventListener('mouseenter', () => openSecondaryFor(item));
		item.addEventListener('click', (e) => {
			e.preventDefault(); // utile su touch
			openSecondaryFor(item);
		});

		// quando esci da un trigger, chiudi se non vai verso la secondaria
		item.addEventListener('mouseleave', (e) => {
			if (!secondary.contains(e.relatedTarget)) {
				closeSecondary();
			}
		});
	});

	// quando esci dalla secondaria, chiudi se non torni su un trigger
	secondary.addEventListener('mouseleave', (e) => {
		if (![...items].some(item => item.contains(e.relatedTarget))) {
			closeSecondary();
		}
	});

	/* --- chiusure globali --- */
	document.addEventListener('click', (e) => {
		if (!sidebar.contains(e.target) && !secondary.contains(e.target)) {
			closeSecondary();
		}
	});
	document.addEventListener('keydown', (e) => {
		if (e.key === 'Escape') closeSecondary();
	});

	/* --- prevenzione scroll-chain --- */
	secondaryInner.addEventListener('wheel', function(e) {
		if (!secondary.classList.contains('active')) return;
		const delta = e.deltaY;
		const atTop = this.scrollTop === 0;
		const atBottom = Math.abs(this.scrollHeight - this.clientHeight - this.scrollTop) <= 1;

		if ((delta < 0 && atTop) || (delta > 0 && atBottom)) {
			e.preventDefault();
		}
	}, { passive: false });

	let touchStartY = 0;
	secondaryInner.addEventListener('touchstart', (e) => {
		if (!secondary.classList.contains('active')) return;
		touchStartY = e.touches[0].clientY;
	}, { passive: true });

	secondaryInner.addEventListener('touchmove', function(e) {
		if (!secondary.classList.contains('active')) return;
		const currentY = e.touches[0].clientY;
		const delta = touchStartY - currentY;
		const atTop = this.scrollTop === 0;
		const atBottom = Math.abs(this.scrollHeight - this.clientHeight - this.scrollTop) <= 1;

		if ((delta < 0 && atTop) || (delta > 0 && atBottom)) {
			e.preventDefault();
		}
	}, { passive: false });
});