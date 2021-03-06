// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // Create Comment from request.
        Comment c = Comment.makeComment(request);

        // Set properties in Datastore entity.
        Entity commentEntity = new Entity("comment");
        commentEntity.setProperty("timestamp", c.getTimestamp());
        commentEntity.setProperty("author", c.getAuthor());
        commentEntity.setProperty("text", c.getText());
        datastore.put(commentEntity);

        // Return the comment ID.
        Gson gson = new Gson();
        response.setContentType("text/plain");
        response.getWriter().println(commentEntity.getKey().getId());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        // Get comment limit parameter.
        int max = Integer.parseInt(request.getParameter("max"));

        // Get page number.
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            response.setStatus(404);
            return;
        }
        
        ArrayList<Comment> comments = new ArrayList<>();

        // Create a query for the Comments.
        Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        // Skip earlier pages and enforce Comment limit.
        FetchOptions options = FetchOptions.Builder.withLimit(max);
        options.offset((page - 1) * max);

        for (Entity entity : results.asIterable(options)) {
            comments.add(Comment.makeComment(entity));
        }

        // Send JSON back to site.
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(comments));
    }
}
