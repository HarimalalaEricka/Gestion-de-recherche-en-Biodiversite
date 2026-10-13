(() => {
    const theme = localStorage.getItem('theme');

    if (theme === 'dark' || theme === 'light') {
        document.documentElement.dataset.bsTheme = theme;
    }
})();
