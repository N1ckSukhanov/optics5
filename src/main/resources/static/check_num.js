function check_num(value) {
    if (isNaN(Number(value.val()))) {
        let len = value.val().length
        if (len > 1) {
            value.val(value.val().substring(0, len - 1))
        } else {
            value.val("")
        }
    }
    return value.val();
}

let price = $("#Цена")
price.on("keyup", () => check_num(price))

let orderId = $("#Номер")
orderId.on("keyup", () => check_num(orderId))