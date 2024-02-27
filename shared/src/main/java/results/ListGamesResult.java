package results;

import requests.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public record ListGamesResult(ArrayList<Game> games) {

}
