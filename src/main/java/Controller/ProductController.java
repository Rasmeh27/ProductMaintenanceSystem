package Controller;

import Service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Products;

import java.util.List;

@Path("/productos")
public class ProductController {
    private final ProductService productService = new ProductService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        List<Products> products = productService.getAllProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") Long id) {
        Products product = productService.getProductById(id);
        if (product != null) {
            return Response.ok(product).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Products product) {
        productService.saveProduct(product);
        return Response.status(Response.Status.CREATED).entity("Producto creado con éxito").build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") Long id, Products updatedProduct) {
        Products existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            updatedProduct.setId(id); // Asegúrate de que el ID no cambie
            productService.updateProduct(updatedProduct);
            return Response.ok("Producto actualizado con éxito").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        Products product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return Response.ok("Producto eliminado con éxito").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Producto no encontrado").build();
        }
    }
}
