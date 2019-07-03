

function getAllRecords(value) {
    var table = $('#dealsTable').DataTable({
        "sAjaxSource": "/fetch/"+value,
        "sAjaxDataProp": '',
        "pageLength": 100,
        "ordering": false,
        "scrollX": true,
        "scrollY": 380,
        "aoColumns": [
            {"data": "uniqueDealId"},
            {"data": "fromCurrencyISO"},
            {"data": "toCurrencyISO"},
            {"data": "date"},
            {"data": "amount"},
            {"data": "fileName"},
        ]
    })
}