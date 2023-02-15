1 syntax on
2 set nu
3 set ruler
4 set showmode
5 set hlsearch
6 set backspace=2
7 set bg=dark
8 set tabstop=4
9 set softtabstop=4
10 set shiftwidth=4
11 set autoindent
12 "imap {<CR> {<CR>}<ESC>O"
13 inoremap ( ()<Esc>i
14 inoremap [ []<Esc>i
15 inoremap { {}<Esc>i
16 "inoremap { {<CR>}<ESC>i"

PS1='\[\033[01;37m\][\[\033[01;34m\]\u\[\033[01;37m\]@\[\033[01;34m\]\h\[\033[01;37m\] \w\[\033[01;37m\]]\[\033[01;35m\]$ \[\033[0m\]'
