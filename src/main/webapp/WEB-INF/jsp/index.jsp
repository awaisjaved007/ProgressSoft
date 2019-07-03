<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<title>Progress Soft Assignment</title>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link th:href="@{/css/style.css}" rel="stylesheet"/>

<script type="text/javascript">
    function getAllRecords(value) {
        $(document).ready(function () {
            var table = $('#dealsTable').DataTable({
                "sAjaxSource": "/fetch/" + value,
                "sAjaxDataProp": '',
                "pageLength": 100,
                "ordering": false,
                "scrollX": true,
                "retrieve": true,
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
        });
    }
</script>
<body>
<h2>Fx Deals</h2>

<div class="row">
    <div class="col-sm-6">
        <form class="upload-form" method="POST" enctype="multipart/form-data" id="fileUploadForm"
              action="/upload">
            <h4 class="upload-form-heading">Upload FX Deals csv file</h4>
            <div class="form-group">
                <%--<label class="control-label" for="uploadFile">Upload File:</label>--%>
                <input type="file" accept=".csv"
                       class="form-control" id="uploadFile" placeholder="Select file"
                       name="file" required/>
            </div>
            <div class="text-right">
                <button type="submit" class="form-button btn btn-default" id="btnSubmit">Upload</button>
            </div>
        </form>
    </div>
</div>
<div>

    <form>
        Select a Category:&nbsp;
        <select name="category" var="selectedValue" onchange="getAllRecords('test.csv')">
            <c:forEach items="${fileNames}" var="fileName">
                <option value="NONE">Select</option>
                <option value="${fileName}">${fileName}</option>
            </c:forEach>
        </select>
        <br/><br/>

        <table id="dealsTable" class="display excel-table">
            <thead>
            <tr>
                <th>#</th>
                <th>From ISO CODE</th>
                <th>TO ISO CODE</th>
                <th>TIME STAMP</th>
                <th>AMOUNT</th>
                <th>FILE NAME</th>
            </tr>
            </thead>
        </table>
    </form>
</div>
</body>
</html>
