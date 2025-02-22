package com.qualityplus.skills.base.commands;

//@Component
public final class SetCommand /*extends AssistantCommand*/ {
  /*  private @Inject Box box;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 4) {

            Player player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidPlayer));
                return false;
            }

            Skill skill = Skills.getByID(args[2]);
            Stat stat = Stats.getByID(args[2]);

            CommonObject object = skill != null ? skill : stat;

            if (object == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().skillsMessages.invalidObject));
                return false;
            }

            Integer level = MathUtils.intOrNull(args[3]);

            if (level == null) {
                sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.invalidAmount));
                return false;
            }

            Optional<UserData> data = box.service().getSkillsData(player.getUniqueId());

            data.ifPresent(userData -> userData.getSkills().addLevel(object.getId(), level));

            List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("amount", level)
                    , new Placeholder("object", object.getId())
                    , new Placeholder("player", player.getName())
                    , new Placeholder("new_level", data.map(d -> d.getSkills().getLevel(object.getId())).orElse(0)));

            sender.sendMessage(StringUtils.processMulti(box.files().messages().skillsMessages.addedAmount, placeholders));

        } else
            sender.sendMessage(StringUtils.color(box.files().messages().pluginMessages.useSyntax));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 2 ? null : args.length == 3 ?
                new ListBuilder<String>()
                        .with(Stats.values().stream().map(CommonObject::getId).collect(Collectors.toList()))
                        .with(Skills.values().stream().map(CommonObject::getId).collect(Collectors.toList()))
                        .get()
                : args.length == 4 ? ListUtils.stringSecuence(0, 10) : Collections.emptyList();
    }

    @Delayed(time = MinecraftTimeEquivalent.SECOND)
    public void register(@Inject Box box) {
        TheAssistantPlugin.getAPI().getCommandProvider().registerCommand(this, e -> e.getCommand().setDetails(box.files().commands().addCommand));
    }*/
}
