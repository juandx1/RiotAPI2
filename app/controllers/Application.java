package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Juan Manuel on 1/05/2016.
 */
public class Application extends Controller {

    public Result index() {
        return ok();
    }
}
