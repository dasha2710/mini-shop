<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Storehouse</title>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script>
        function Download(url) {
            document.getElementById('my_iframe').src = url;
        }

        $(function () {
            $("#exportProducts, #exportSections")
                    .button()
                    .click(function (event) {
                        event.preventDefault();
                        var activeInputId = getActiveInputName();
                        var query = $(activeInputId).find(".queryBody").text();
                        var resultQuery = replacePlaceholdersWithValues(query)
                        $("#query").val(resultQuery);
                        Download('/api/export_to_csv?query=' + resultQuery);
                    });

            $("#actionType").selectmenu({
                change: function () {
                    defineVisible();
                }
            });
            $("#collection").selectmenu({
                change: function () {
                    defineVisible();
                }
            });
            $("#submitQuery").button().click(
                    function (event) {
                        var activeInputId = getActiveInputName();
                        var query = $(activeInputId).find(".queryBody").text();
                        $("#query").val(replacePlaceholdersWithValues(query));
                    }
            );
            $("#sendQuery")
                    .button()
                    .click(function (event) {
                        event.preventDefault();
                        $.ajax({
                            url: "/api/dsl?query=" + $("#query").val(),
                            type: "get",
                            contentType: "application/json; charset=utf-8",
                            success: function (data) {
                                $("#products").hide();
                                $("#sections").hide();
                                if (data.message != null) {
                                    alert(data.message);
                                }
                                if (data.products != null) {
                                    $("#productsBody").html("");
                                    $.each(data.products, function (index, results) {

                                        $("#productsBody").append(
                                                "<tr>"
                                                + "<td>" + results.title + "</td>"
                                                + "<td>" + results.code + "</td>"
                                                + "<td>" + results.price + "</td>"
                                                + "<td>" + results.producerCountry + "</td>"
                                                + "<td>" + results.section.name + "</td>"
                                                + "<td>" + results.section.description + "</td>"
                                                + "</tr>")
                                    });
                                    $("#products").show();
                                }
                                if (data.product != null) {
                                    $("#productsBody").html("");
                                    $("#productsBody").append(
                                            "<tr>"
                                            + "<td>" + data.product.title + "</td>"
                                            + "<td>" + data.product.code + "</td>"
                                            + "<td>" + data.product.price + "</td>"
                                            + "<td>" + data.product.producerCountry + "</td>"
                                            + "<td>" + data.product.section.name + "</td>"
                                            + "<td>" + data.product.section.description + "</td>"
                                            + "</tr>");

                                    $("#products").show();
                                }
                                if (data.sections != null) {
                                    $("#sectionsBody").html("");
                                    $.each(data.sections, function (index, results) {

                                        $("#sectionsBody").append(
                                                "<tr>"
                                                + "<td>" + results.name + "</td>"
                                                + "<td>" + results.description + "</td>"
                                                + "</tr>")
                                    });
                                    $("#sections").show();
                                }
                                if (data.section != null) {
                                    $("#sectionsBody").html("");
                                    $("#sectionsBody").append(
                                            "<tr>"
                                            + "<td>" + data.section.name + "</td>"
                                            + "<td>" + data.section.description + "</td>"
                                            + "</tr>");
                                    $("#sections").show();
                                }
                            },
                            error: function (xhr) {
                                alert("Error: " + xhr.responseText);
                            }
                        });
                    })
        });

        function defineVisible() {
            $(".queryParams").hide();
            var activeInputId = getActiveInputName();
            $(activeInputId).show();
        }
        function getActiveInputName() {
            var action = $("#actionType").val();
            var col = $("#collection").val();
            return ("#" + action + col).replace(/\s/g, "");
        }
        function replacePlaceholdersWithValues(input) {
            return input.replace(/\[.*?\]/g, function replaceParam(phrase) {
                var paramName = /#[a-zA-Z]+/g.exec(phrase)[0];
                var paramValue = $(paramName).val();
                if (paramValue != "") {
                    return phrase
                            .replace(/[\[\]]/g, "")
                            .replace(new RegExp(paramName, ""), paramValue);
                }
                return "";
            });
        }
    </script>
    <style>
        select {
            width: 200px;
        }

        .queryBody {
            display: none;
        }
    </style>
</head>
<body>

<div align="center">

<h1>Storehouse dsl</h1>

</br>
<form action="#">
    <select name="actionType" id="actionType">
        <option selected="selected">Select</option>
        <option>Select All</option>
        <option>Add</option>
        <option>Delete</option>
        <option>Update</option>
    </select>
    <select name="collection" id="collection">
        <option selected="selected">Product</option>
        <option>Section</option>
    </select>

    <div id="SelectProduct" class="queryParams">
        with title <input id="selectProductTitle"/>
        code <input id="selectProductCode"/>

        <div class="queryBody">Select product with[ title #selectProductTitle][ code #selectProductCode]</div>
    </div>
    <div id="SelectSection" class="queryParams">
        with name <input id="selectSectionName"/>

        <div class="queryBody">Select section with[ name #selectSectionName]</div>
    </div>

    <div id="SelectAllProduct" class="queryParams">
        for section <input id="selectAllProductSectionName"/>

        <div class="queryBody">Select all products[ for section #selectAllProductSectionName]</div>
        <div align="right">
            <input id="exportProducts" type="submit" value="export products to xls file"/>
        </div>
    </div>
    <div id="SelectAllSection" class="queryParams">
        <div class="queryBody">Select all sections</div>
        <div align="right">
            <input id="exportSections" type="submit" value="export sections to xls file"/>
        </div>
    </div>

    <div id="AddProduct" class="queryParams">
        to <input id="addProductCategory"/>
        with title <input id="addProductTitle"/>
        code <input id="addProductCode"/>
        price <input id="addProductPrice"/>
        producer country <input id="addProductProducerCountry"/>

        <div class="queryBody">Add product[ to #addProductCategory] with[ title #addProductTitle][ code #addProductCode][ price #addProductPrice][ producer_country #addProductProducerCountry]
        </div>
    </div>
    <div id="AddSection" class="queryParams">
        with name <input id="addSectionName"/>
        description <input id="addSectionDescription"/>

        <div class="queryBody">Add section with[ name #addSectionName][ description #addSectionDescription]</div>
    </div>

    <div id="DeleteProduct" class="queryParams">
        <input id="deleteProductCode"/>

        <div class="queryBody">Delete product[ #deleteProductCode]</div>
    </div>
    <div id="DeleteSection" class="queryParams">
        <input id="deleteSectionName"/>

        <div class="queryBody">Delete section[ #deleteSectionName]</div>
    </div>

    <div id="UpdateProduct" class="queryParams">
        <input id="updateProductCodeOld"/>
        with title <input id="updateProductTitle"/>
        code <input id="updateProductCode"/>
        price <input id="updateProductPrice"/>
        producer country <input id="updateProductProducerCountry"/>

        <div class="queryBody">Update product[ #updateProductCodeOld] with[ title #updateProductTitle][ code #updateProductCode][ price #updateProductPrice][ producer_country #updateProductProducerCountry]
        </div>
    </div>
    <div id="UpdateSection" class="queryParams">
        <input id="updateSectionNameOld"/>
        with name <input id="updateSectionName"/>
        description <input id="updateSectionDescription"/>

        <div class="queryBody">Update section[ #updateSectionNameOld] with[ name #updateSectionName][ description #updateSectionDescription]
        </div>
    </div>
</form>
<button id="submitQuery">Build query</button>
<br/>
<textarea id="query" rows="4" cols="150" title="Query"></textarea>
<br/>
<input id="sendQuery" type="submit" value="Send query"/>

<table id="products" class="display" cellspacing="0" width="100%" hidden="hidden">
    <thead>
    <tr>
        <th>Title</th>
        <th>Code</th>
        <th>Price</th>
        <th>Producer Country</th>
        <th>Section name</th>
        <th>Section description</th>
    </tr>
    </thead>
    <tbody id="productsBody">
    </tbody>
</table>

<table id="sections" class="display" cellspacing="0" width="50%" hidden="hidden">
    <thead>
    <tr>
        <th>Name</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody id="sectionsBody">
    </tbody>
</table>
</div>
<iframe id="my_iframe" style="display:none;"></iframe>
<script>
    defineVisible();
</script>
</body>
</html>