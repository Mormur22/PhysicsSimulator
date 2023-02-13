package simulator.launcher;

import org.apache.commons.cli.*;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Double _stimeDefaultValue = 150.0;
	private final static Double _epsValue = 0.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static int _steps;
	private static Double _dtime = null;
	private static Double _stime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static PrintStream _outConsole = null;
	private static String _expOutFile = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;


	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		
		//initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		
		// TODO initialize the force laws factory
		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);
		
		// TODO initialize the state comparator
		ArrayList<Builder<StateComparator>> statecomBuilders = new ArrayList<>();
		statecomBuilders.add(new EpsilonEqualStateBuilder());
		statecomBuilders.add(new MassEqualStateBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(statecomBuilders);
		
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			
			//////////////////////////////////////
			parseOutFileOption(line);
			parseExpOutputFileOption(line);
			parseSteptimeOption(line);
			//////////////////////////////////////

			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static void parseModeOption(CommandLine line) {
		_mode = line.getOptionValue("m", _modeDefaultValue);
		if(!_mode.equals("gui"))
			_mode = "batch";
	}

	private static void parseSteptimeOption(CommandLine line) throws ParseException {
		String st = line.getOptionValue("s", _stimeDefaultValue.toString());
		try {
			_steps= (int) Double.parseDouble(st);
			assert (_stime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + st);
		}
	}

	private static void parseExpOutputFileOption(CommandLine line) {
		_expOutFile = line.getOptionValue("eo");
	}

	private static void parseOutFileOption(CommandLine line) {
		_outFile = line.getOptionValue("o");
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// TODO add support for -o, -eo, and -s (add corresponding information to
		// cmdLineOptions)
		
		// o
        cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
                .desc("Output file, where output is written. Default value: the standard output.").build());

        // eo
        cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg()
                .desc("The expected output file. If not provided no comparison is applied.").build());

        // s
        cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg()
                .desc("An integer representing the number of simulation steps. Default value: " +
                		_stimeDefaultValue+ ".").build());
		//m
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: ’batch’ " +
				"(Batch mode), ’gui’ (Graphical User " +
				"Interface mode). Default value: ’batch’.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && _mode.equals("batch")) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
			
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		
		PhysicsSimulator sim = new PhysicsSimulator(_dtime,_forceLawsFactory.createInstance(_forceLawsInfo));
		Controller ctrl= new Controller(sim, _bodyFactory, _forceLawsFactory);
	
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream os= _outFile ==null ?
				System.out : new FileOutputStream(new File(_outFile));
		
		InputStream expOut = null;
		StateComparator stateCmp= null;
	
		if(_expOutFile != null) {
	
			expOut = new FileInputStream(new File(_expOutFile));
			stateCmp= _stateComparatorFactory.createInstance(_stateComparatorInfo);
			
		}

		ctrl.loadBodies(in);
		ctrl.run(_steps, os , expOut, stateCmp);

	}

	private static void startGUIMode() throws Exception {

		PhysicsSimulator sim = new PhysicsSimulator(_dtime, _forceLawsFactory.createInstance(_forceLawsInfo));
		Controller ctrl = new Controller(sim, _bodyFactory, _forceLawsFactory);

		if (_inFile != null) {
			InputStream in = new FileInputStream(new File(_inFile));
			ctrl.loadBodies(in);
		}

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
	}

	public static void main(String[] args)

	{
		init();
		parseArgs(args);
	
		if(_mode.equals("gui")){
			try {
				startGUIMode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		else {
			try {
				startBatchMode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}

	}

}