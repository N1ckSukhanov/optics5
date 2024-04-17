function add_space(value, num) {
    if (value.length > num && value[num] !== " ")
        value = value.substring(0, num) + " " + value.substring(num)
    return value
}

function checkPhone(field) {
    let value = field.val()
    if (!value || value.length < 2 || !isNaN(Number(value[0])))
        value = "+7"
    if (isNaN(Number(value.at(value.length - 1))))
        value = value.substring(0, value.length - 1)

    value = add_space(value, 2)
    value = add_space(value, 6)
    value = add_space(value, 10)
    value = add_space(value, 13)

    if (value.length >= 16)
        value = value.substring(0, 16)

    value = value.trim()

    field.val(value)
}

function checkName(field) {
    let value = field.val()
    if (!value)
        return
    value = value.replace(value[0], value.at(0).toUpperCase())
    let index = value.indexOf(" ")
    if (index !== -1 && value.length > index + 1)
        value = value.replace(value[index + 1], value.at(index + 1).toUpperCase())

    field.val(value)
}

function checkSearch(field) {
    let search = field.val()
    if (!search)
        return
    if (!isNaN(Number(search[0])) || search[0] === "+")
        checkPhone(field)
    else
        checkName(field)
}

let phone = $("#Телефон")
phone.on("keyup", () => checkPhone(phone))

let surname = $("#Имя")
surname.on("keyup", () => checkName(surname))

let search = $("#search")
search.on("keyup", () => checkSearch(search))