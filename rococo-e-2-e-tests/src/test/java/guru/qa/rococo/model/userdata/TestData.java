package guru.qa.rococo.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record TestData(@JsonIgnore String password) {}
