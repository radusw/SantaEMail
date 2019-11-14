function run() {
    var tableData = new Array();
    $('#dataTable tr').each(function(row, tr) { if(row != 0) {
        tableData[row-1] = {
            "name" : $(tr).find('td:eq(1)').find("input").val(),
            "email": $(tr).find('td:eq(2)').find("input").val()
        }
    }});

    $.ajax({
        url: '/api/run',
        type: 'POST',
        data: JSON.stringify(tableData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(data, textStatus) {
            $('#msg').text("Done!");
            var persons = "";
            $.each(data, function(idx, value) {
                console.log(value);
                persons += value.name + " (" + value.email + ")</br>";
            });
            $('#data').html("<p>I've just sent emails to:</br>" + persons + "</p>");
        }
    });
}

function deleteRow(row) {
    var i = row.parentNode.parentNode.rowIndex;
    if(i != 1)
        document.getElementById('dataTable').deleteRow(i);
}

function insRow() {
    var x=document.getElementById('dataTable');
    var new_row = x.rows[1].cloneNode(true);
    var len = x.rows.length;
    new_row.cells[0].innerHTML = len;

    var inp1 = new_row.cells[1].getElementsByTagName('input')[0];
    inp1.id += len;
    inp1.value = '';
    var inp2 = new_row.cells[2].getElementsByTagName('input')[0];
    inp2.id += len;
    inp2.value = '';
    x.appendChild(new_row);
}