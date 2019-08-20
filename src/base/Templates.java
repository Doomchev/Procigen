package base;

import static base.Main.addNew;
import static base.Main.alterations;
import static base.Main.compositionTypes;
import static base.Main.palettes;
import static base.Main.patternTypes;
import static base.Main.project;
import static base.Main.transformationTypes;
import parameters.DoubleValue;
import structure.Bitmap;
import structure.Palette;
import structure.Project;
import structure.alterations.ChainedLinearAlteration;
import structure.alterations.ChainedSineAlteration;
import structure.alterations.LinearAlteration;
import structure.alterations.SineAlteration;
import structure.compositions.CompositionArray;
import structure.compositions.SingleComposition;
import structure.patterns.Beam;
import structure.patterns.Gradient;
import structure.transformations.Affine;
import structure.transformations.LoopSector;
import structure.transformations.MoveToVector;
import structure.transformations.Radial;
import structure.transformations.Swirl;

public class Templates {
  public static void init() {
    palettes.add(new Palette("Black to white", new Palette.Col(1, 0, 0, 0)
        , new Palette.Col(1, 255, 255, 255)));
    palettes.add(new Palette("Fire", new Palette.Col(1, 0, 0, 0)
        , new Palette.Col(1, 255, 255, 255), new Palette.Col(1, 255, 255, 255)
        , new Palette.Col(2, 255, 255, 0), new Palette.Col(4, 255, 0, 0)
        , new Palette.Col(2, 0, 0, 0)));
    palettes.add(new Palette("Looped fire", new Palette.Col(2, 255, 255, 255)
        , new Palette.Col(2, 255, 255, 0), new Palette.Col(2, 255, 0, 0)
        , new Palette.Col(2, 0, 0, 0), new Palette.Col(2, 255, 0, 0)
        , new Palette.Col(2, 255, 255, 0)));
    palettes.add(new Palette("Frost", new Palette.Col(2, 255, 255, 255)
        , new Palette.Col(2, 0, 255, 255), new Palette.Col(2, 0, 0, 255)
        , new Palette.Col(2, 0, 0, 0), new Palette.Col(2, 0, 0, 255)
        , new Palette.Col(2, 0, 255, 255)));
    palettes.add(new Palette("Pale fire", new Palette.Col(2, 255, 255, 255)
        , new Palette.Col(2, 255, 255, 0), new Palette.Col(2, 255, 0, 128)
        , new Palette.Col(2, 0, 0, 128), new Palette.Col(2, 255, 0, 128)
        , new Palette.Col(2, 255, 255, 0)));
    palettes.add(new Palette("Blue and orange", new Palette.Col(1, 26, 42, 108)
        , new Palette.Col(1, 178, 31, 31), new Palette.Col(2, 253, 187, 45)));

    addNew(patternTypes, new Gradient());
    addNew(patternTypes, new Beam());

    addNew(compositionTypes, new SingleComposition());
    addNew(compositionTypes, new CompositionArray());

    addNew(transformationTypes, new Affine());
    addNew(transformationTypes, new LoopSector());
    addNew(transformationTypes, new MoveToVector());
    addNew(transformationTypes, new Radial());
    addNew(transformationTypes, new Swirl());

    addNew(alterations, new DoubleValue(0.0));
    addNew(alterations, new LinearAlteration());
    addNew(alterations, new SineAlteration());
    addNew(alterations, new ChainedLinearAlteration());
    addNew(alterations, new ChainedSineAlteration());

    project = new Project();
    project.init();
    Bitmap bitmap = new Bitmap();
    addNew(project, Project.ELEMENTS, bitmap);
    addNew(bitmap, Bitmap.COMPOSITIONS, new CompositionArray());    
  }
}
