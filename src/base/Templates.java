package base;

import static base.Main.addNew;
import static base.Main.alterations;
import static base.Main.compositionTypes;
import static base.Main.patternTypes;
import static base.Main.transformationTypes;
import java.io.File;
import java.util.LinkedList;
import parameters.DoubleValue;
import structure.Bitmap;
import structure.Element;
import structure.Options;
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
import structure.transformations.MultiplyAngle;
import structure.transformations.LoopSector;
import structure.transformations.MoveToVector;
import structure.transformations.Radial;
import structure.transformations.Sine;
import structure.transformations.Swirl;

public class Templates {
  public static void init() {
    /*Options.instance.init();
    LinkedList<Element> palettes = Options.getPalettes();
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
    palettes.add(new Palette("Orange fire", new Palette.Col(1, 0, 0, 0)
        , new Palette.Col(1, 245, 159, 50)));
    palettes.add(new Palette("Carpet fire", new Palette.Col(1, 0, 0, 0)
        , new Palette.Col(1, 100, 0, 0), new Palette.Col(1, 241, 217, 0)));
    palettes.add(new Palette("Purple and orange", new Palette.Col(1, 0, 0, 0)
        , new Palette.Col(1, 114, 47, 162), new Palette.Col(1, 255, 255, 255)
        , new Palette.Col(1, 122, 18, 9), new Palette.Col(1, 255, 237, 58)
        , new Palette.Col(1, 122, 18, 9)));*/
    Options.instance.init();
    
    addNew(patternTypes, new Gradient());
    addNew(patternTypes, new Beam());

    addNew(compositionTypes, new SingleComposition());
    addNew(compositionTypes, new CompositionArray());

    addNew(transformationTypes, new Affine());
    addNew(transformationTypes, new LoopSector());
    addNew(transformationTypes, new MoveToVector());
    addNew(transformationTypes, new Radial());
    addNew(transformationTypes, new Swirl());
    addNew(transformationTypes, new Sine());
    addNew(transformationTypes, new MultiplyAngle());

    addNew(alterations, new DoubleValue(0.0));
    addNew(alterations, new LinearAlteration());
    addNew(alterations, new SineAlteration());
    addNew(alterations, new ChainedLinearAlteration());
    addNew(alterations, new ChainedSineAlteration());

    Serialization.load(new File("configuration.bin"), false);
    
    Project.instance = new Project();
    Project.instance.init();
    Bitmap bitmap = new Bitmap();
    addNew(Project.instance, Project.ELEMENTS, bitmap);
    addNew(bitmap, Bitmap.COMPOSITIONS, new CompositionArray());
  }
}
