package com.cqupt.software_10.entity.tasks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    public String name;
    public String model_type;
    public Params params;
    public Res res;
}

