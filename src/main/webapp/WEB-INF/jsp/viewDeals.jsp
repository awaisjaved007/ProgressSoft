<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<title>Progress Soft Assignment</title>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>

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
    <div class="text-right">
        <input type="button" class="form-button btn"  id="btnViewDeal">Upload</input>
    </div>
</div>

<table border="1">

    <c:forEach var="fileName" items="${fileNames}" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td>${fileName}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
