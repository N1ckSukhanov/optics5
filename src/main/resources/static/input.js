const inputs = $("form input").get();

$("form").on("keydown", "input, select", function (e) {
    if (e.which === 13) {
        const which = inputs.indexOf(this),
            next = inputs[which + 1]
        if (next) {
            next.focus()
        }
        e.preventDefault()
    }
})