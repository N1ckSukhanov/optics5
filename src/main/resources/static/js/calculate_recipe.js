function add(all, value) {
    if (value)
        all += parseInt(value)
    return all
}

function calculate_sum(values, last) {
    let all = 0
    for (let value of values) {
        let v = check_num(value)
        all = add(all, v)
    }
    last.val(all)
}

function calculate_diff(values, last) {
    let all = 0
    let v = check_num(values[0])
    all = add(all, v)
    v = check_num(values[1])
    all = add(all, -v)
    last.val(all)
}

let frame = $("#Оправа")
let lenses = $("#Линзы")
let work = $("#Работа")
let sum = $("#Сумма")
let discount = $("#Скидка")
let total = $("#Итого")
let paid = $("#Оплачено")
let extra = $("#Доплата")

frame.on("keyup", () => calculateSum())
lenses.on("keyup", () => calculateSum())
work.on("keyup", () => calculateSum())

sum.on("keyup", () => calculateTotal())
discount.on("keyup", () => calculateTotal())
total.on("keyup", () => calculateTotal())

total.on("keyup", () => calculateExtra())
paid.on("keyup", () => calculateExtra())

function calculateSum() {
    calculate_sum([frame, lenses, work], sum)
}

function calculateTotal() {
    let s = check_num(sum)
    if (s){
        let disc = check_num(discount)
        if (disc < 100)
            total.val(parseInt(s * (1 - 0.01 * disc)))
        else
            total.val(parseInt(s - disc))
    }

}

// total.val(parseInt(s * (1 - 0.01 * parseInt(discount.text()))))
function calculateExtra() {
    calculate_diff([total, paid], extra)
}