export const HomeIcon = ({
    fill = "currentColor",
    filled,
    ...props
}: any) => {
    if (filled) {
        return (
            <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='#000000' width='24' height='24' {...props}>
                <path
                    d="M12.74 2.32a1 1 0 0 0-1.48 0l-9 10A1 1 0 0 0 3 14h2v7a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-7h2a1 1 0 0 0 1-1 1 1 0 0 0-.26-.68z"
                    fill={fill}
                >
                </path>
            </svg>)
    } else {
        return (
            <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='#000000' width='24' height='24' {...props}>
                <path
                    d="M12.71 2.29a1 1 0 0 0-1.42 0l-9 9a1 1 0 0 0 0 1.42A1 1 0 0 0 3 13h1v7a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-7h1a1 1 0 0 0 1-1 1 1 0 0 0-.29-.71zM6 20v-9.59l6-6 6 6V20z"
                    fill={fill}
                >
                </path>
            </svg>)
    }
}